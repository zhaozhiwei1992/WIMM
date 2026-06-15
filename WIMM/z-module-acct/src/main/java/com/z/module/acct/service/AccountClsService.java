package com.z.module.acct.service;

import com.z.framework.common.util.GenericTreeBuilderUtil;
import com.z.framework.common.web.vo.SelectOptionVO;
import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.web.mapper.AccountClsSelectMapper;
import com.z.module.acct.web.vo.AccountClsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 会计科目管理 Service
 */
@Service
@Slf4j
public class AccountClsService {

    private final AccountClsRepository accountClsRepository;
    private final AccountClsSelectMapper accountClsSelectMapper;

    public AccountClsService(AccountClsRepository accountClsRepository,
                             AccountClsSelectMapper accountClsSelectMapper) {
        this.accountClsRepository = accountClsRepository;
        this.accountClsSelectMapper = accountClsSelectMapper;
    }

    /**
     * 新增会计科目
     */
    public AccountCls createAccountCls(AccountCls accountCls) {
        if (Objects.isNull(accountCls.getParentId())) {
            accountCls.setParentId(0L);
        }
        return accountClsRepository.save(accountCls);
    }

    /**
     * 查询所有会计科目（树形结构）
     */
    public List<AccountClsVO> getAllAccountClsTree(AccountCls filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true)
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withIgnorePaths("id", "createdDate", "lastModifiedDate");

        Example<AccountCls> ex = Example.of(filter, matcher);
        List<AccountCls> allList = accountClsRepository.findAll(ex);

        List<AccountClsVO> collect = allList.stream().map(m -> {
            AccountClsVO vo = new AccountClsVO();
            BeanUtils.copyProperties(m, vo);
            return vo;
        }).collect(Collectors.toList());

        GenericTreeBuilderUtil<AccountClsVO> treeBuilder = new GenericTreeBuilderUtil<>(AccountClsVO.class);
        return treeBuilder.buildTree(collect);
    }

    /**
     * 根据ID查询会计科目
     */
    public AccountCls getAccountCls(Long id) {
        return accountClsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会计科目不存在, id: " + id));
    }

    /**
     * 批量删除会计科目
     */
    public void deleteAccountCls(List<Long> idList) {
        accountClsRepository.deleteAllByIdIn(idList);
    }

    /**
     * 获取所有一级科目
     */
    public List<AccountCls> getAllRootAccountCls() {
        return accountClsRepository.findAllByParentIdOrderByOrderNumAsc(0L);
    }

    /**
     * 获取科目树 Select 选项
     */
    public List<SelectOptionVO> getAccountClsSelect() {
        List<AccountCls> accountClsList = accountClsRepository.findAll();
        List<SelectOptionVO> convert = accountClsSelectMapper.convert(accountClsList);
        GenericTreeBuilderUtil<SelectOptionVO> treeBuilder = new GenericTreeBuilderUtil<>(SelectOptionVO.class);
        return treeBuilder.buildTree(convert);
    }
}
