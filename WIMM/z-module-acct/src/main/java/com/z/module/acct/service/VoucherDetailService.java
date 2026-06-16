package com.z.module.acct.service;

import com.z.framework.security.util.SecurityUtils;
import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.repository.VoucherDetailRepository;
import com.z.module.acct.web.mapper.VoucherDetailMapper;
import com.z.module.acct.web.vo.VoucherDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 凭证分录 Service
 * <p>
 * 多租户隔离: 查询/删除均限定当前家庭的 tenant_id.
 * 不再依赖 CustomStatementInspector 的 SQL 改写(避免占位符/重写风险).
 */
@Service
@Slf4j
public class VoucherDetailService {

    private final VoucherDetailRepository voucherDetailRepository;
    private final VoucherDetailMapper voucherDetailMapper;

    public VoucherDetailService(VoucherDetailRepository voucherDetailRepository,
                                VoucherDetailMapper voucherDetailMapper) {
        this.voucherDetailRepository = voucherDetailRepository;
        this.voucherDetailMapper = voucherDetailMapper;
    }

    /**
     * 分页查询凭证分录.
     * 家庭模式(acctCate=true): 看当前家庭所有人的明细(按 tenant_id);
     * 个人模式(其他): 只看自己录入的明细(在 tenant_id 基础上再加 created_by).
     */
    public Map<String, Object> getAllVoucherDetail(Pageable pageable, VoucherDetail voucherDetail) {
        // 强制按当前家庭隔离
        voucherDetail.setTenantId(SecurityUtils.getTenantId());
        // 个人模式只看自己
        if (!isFamilyMode()) {
            voucherDetail.setCreatedBy(SecurityUtils.getCurrentLoginName());
        }

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true)
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id", "createdDate", "lastModifiedDate");

        Example<VoucherDetail> ex = Example.of(voucherDetail, matcher);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "voucherNo", "drCr");
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), sort);
        Page<VoucherDetail> all = voucherDetailRepository.findAll(ex, pageable);

        return new HashMap<>() {{
            put("list", all.getContent());
            put("total", all.getTotalElements());
        }};
    }

    /**
     * 是否家庭记账模式: 前端通过 acctCate header 传入(true=家庭, 其他=个人).
     */
    private boolean isFamilyMode() {
        return "true".equalsIgnoreCase(SecurityUtils.getAcctCate());
    }

    /**
     * 按ID批量删除所属交易的整笔凭证(校验归属)
     */
    public void deleteByVoucherNo(List<Long> idList) {
        String tenantId = SecurityUtils.getTenantId();
        // 仅取属于当前家庭的分录, 防止越权删除
        List<VoucherDetail> allById = voucherDetailRepository.findAllById(idList).stream()
                .filter(v -> Objects.equals(v.getTenantId(), tenantId))
                .toList();
        if (allById.isEmpty()) {
            return;
        }
        List<String> voucherNos = allById.stream().map(VoucherDetail::getVoucherNo).toList();
        voucherDetailRepository.deleteAllByVoucherNoInAndTenantId(voucherNos, tenantId);
    }

    /**
     * 查询所有分录用于导出(当前家庭; 个人模式只导出自己)
     */
    public List<VoucherDetailVO> getAllForExport() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "voucherNo", "drCr");
        String tenantId = SecurityUtils.getTenantId();
        List<VoucherDetail> all = voucherDetailRepository
                .findAllByTenantIdOrderByCreatedDateDesc(tenantId, sort);
        // 个人模式只导出自己录入的
        if (!isFamilyMode()) {
            String me = SecurityUtils.getCurrentLoginName();
            all = all.stream().filter(v -> Objects.equals(v.getCreatedBy(), me)).toList();
        }
        return voucherDetailMapper.convert(all);
    }
}
