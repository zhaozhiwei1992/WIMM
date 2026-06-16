package com.z.module.acct.service;

import com.z.framework.common.domain.TenantConstants;
import com.z.framework.common.util.GenericTreeBuilderUtil;
import com.z.framework.common.web.vo.SelectOptionVO;
import com.z.framework.security.util.SecurityUtils;
import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.web.mapper.AccountClsSelectMapper;
import com.z.module.acct.web.vo.AccountClsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 会计科目管理 Service
 * <p>
 * 多租户(家庭)隔离: 所有查询/增删改都限定在当前登录用户的 tenant_id 范围内.
 * 预设科目模板(tenant_id='__template__')在注册时复制到每个家庭, 各家庭独立维护.
 */
@Service
@Slf4j
public class AccountClsService {

    /**
     * 预设科目模板的租户标识, 仅供注册时复制使用, 不出现在任何业务查询结果中.
     * 统一引用 {@link TenantConstants#TEMPLATE_TENANT_ID}.
     */
    public static final String TEMPLATE_TENANT_ID = TenantConstants.TEMPLATE_TENANT_ID;

    private final AccountClsRepository accountClsRepository;
    private final AccountClsSelectMapper accountClsSelectMapper;

    public AccountClsService(AccountClsRepository accountClsRepository,
                             AccountClsSelectMapper accountClsSelectMapper) {
        this.accountClsRepository = accountClsRepository;
        this.accountClsSelectMapper = accountClsSelectMapper;
    }

    /**
     * 新增会计科目(限定本家庭)
     */
    public AccountCls createAccountCls(AccountCls accountCls) {
        if (Objects.isNull(accountCls.getParentId())) {
            accountCls.setParentId(0L);
        }
        // 强制归属当前家庭, 忽略前端传入的 tenantId
        accountCls.setTenantId(SecurityUtils.getTenantId());
        return accountClsRepository.save(accountCls);
    }

    /**
     * 查询当前家庭的会计科目(树形结构, 前端 el-table 树形模式渲染, 默认收起)
     */
    public List<AccountClsVO> getAllAccountClsTree() {
        List<AccountCls> allList = accountClsRepository
                .findAllByTenantIdOrderByOrderNumAsc(SecurityUtils.getTenantId());

        List<AccountClsVO> collect = allList.stream().map(m -> {
            AccountClsVO vo = new AccountClsVO();
            BeanUtils.copyProperties(m, vo);
            return vo;
        }).collect(Collectors.toList());

        GenericTreeBuilderUtil<AccountClsVO> treeBuilder = new GenericTreeBuilderUtil<>(AccountClsVO.class);
        return treeBuilder.buildTree(collect);
    }

    /**
     * 根据ID查询会计科目(校验归属当前家庭)
     */
    public AccountCls getAccountCls(Long id) {
        return accountClsRepository.findById(id)
                .filter(a -> Objects.equals(a.getTenantId(), SecurityUtils.getTenantId()))
                .orElseThrow(() -> new RuntimeException("会计科目不存在或无权访问, id: " + id));
    }

    /**
     * 批量删除会计科目(校验归属 + 禁止删除预设科目)
     */
    public void deleteAccountCls(List<Long> idList) {
        String tenantId = SecurityUtils.getTenantId();
        for (Long id : idList) {
            AccountCls accountCls = accountClsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("会计科目不存在, id: " + id));
            if (!Objects.equals(accountCls.getTenantId(), tenantId)) {
                throw new RuntimeException("无权删除非本家庭科目, id: " + id);
            }
            // 预设科目(一级 + 系统预设二级)不可删除, 只能停用
            if (Integer.valueOf(1).equals(accountCls.getIsStandard())) {
                throw new RuntimeException("预设科目不可删除, 可改为停用: " + accountCls.getName());
            }
        }
        accountClsRepository.deleteAllById(idList);
    }

    /**
     * 获取当前家庭所有一级科目
     */
    public List<AccountCls> getAllRootAccountCls() {
        return accountClsRepository.findAllByTenantIdAndParentIdOrderByOrderNumAsc(
                SecurityUtils.getTenantId(), 0L);
    }

    /**
     * 获取当前家庭科目树 Select 选项
     */
    public List<SelectOptionVO> getAccountClsSelect() {
        List<AccountCls> accountClsList = accountClsRepository
                .findAllByTenantIdOrderByOrderNumAsc(SecurityUtils.getTenantId());
        List<SelectOptionVO> convert = accountClsSelectMapper.convert(accountClsList);
        GenericTreeBuilderUtil<SelectOptionVO> treeBuilder = new GenericTreeBuilderUtil<>(SelectOptionVO.class);
        return treeBuilder.buildTree(convert);
    }

    /**
     * 为新家庭初始化预设科目: 从模板(tenant_id='__template__')复制一份到指定租户.
     * <p>
     * 复制时重新生成 id, code/name/balance_dir/层级关系保持一致;
     * parentId 映射到复制后的新 id(一级仍指向 0).
     * 幂等: 若该租户已有科目则跳过.
     *
     * @param tenantId 新家庭的租户标识
     */
    public void initPresetForTenant(String tenantId) {
        // 幂等: 该家庭已有科目则不重复初始化
        if (!accountClsRepository.findAllByTenantIdOrderByOrderNumAsc(tenantId).isEmpty()) {
            log.info("租户 {} 已存在科目, 跳过预设初始化", tenantId);
            return;
        }

        List<AccountCls> template = accountClsRepository
                .findAllByTenantIdOrderByOrderNumAsc(TEMPLATE_TENANT_ID);
        if (template.isEmpty()) {
            log.warn("预设科目模板为空, 跳过租户 {} 初始化", tenantId);
            return;
        }

        // 模板 id -> 新副本 id 的映射, 用于重建 parent_id 层级
        Map<Long, Long> idMapping = new HashMap<>();
        // 先创建全部节点(暂存新对象), 再回填 parentId
        List<AccountCls> copies = new ArrayList<>();
        for (AccountCls src : template) {
            AccountCls copy = new AccountCls();
            BeanUtils.copyProperties(src, copy);
            copy.setId(null);       // 由数据库自增生成新 id
            copy.setTenantId(tenantId);
            copies.add(copy);
        }
        List<AccountCls> saved = accountClsRepository.saveAll(copies);
        for (int i = 0; i < template.size(); i++) {
            idMapping.put(template.get(i).getId(), saved.get(i).getId());
        }
        // 回填 parentId: 模板 parentId=0(一级) 保持不变, 否则映射到新 id
        for (int i = 0; i < saved.size(); i++) {
            Long srcParentId = template.get(i).getParentId();
            Long newParentId = (srcParentId == null || srcParentId == 0L)
                    ? srcParentId : idMapping.get(srcParentId);
            saved.get(i).setParentId(newParentId);
        }
        accountClsRepository.saveAll(saved);
        log.info("租户 {} 预设科目初始化完成, 共 {} 个", tenantId, saved.size());
    }
}
