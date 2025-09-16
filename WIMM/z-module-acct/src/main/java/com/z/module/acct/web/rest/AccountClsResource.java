package com.z.module.acct.web.rest;

import com.z.framework.common.util.GenericTreeBuilderUtil;
import com.z.framework.common.web.vo.SelectOptionVO;
import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.web.mapper.AccountClsSelectMapper;
import com.z.module.acct.web.vo.AccountClsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: AccountClsResource
 * @Package com/z/module/acct/web/rest/AccountClsResource.java
 * @Description: 账户维护
 * @date 2024/10/28 16:53
 */
@Tag(name = "账户维护API")
@RestController
@RequestMapping("/acct")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AccountClsResource {

    private final AccountClsRepository accountClsRepository;
    private final AccountClsSelectMapper accountClsSelectMapper;

    public AccountClsResource(AccountClsRepository accountClsRepository, AccountClsSelectMapper accountClsSelectMapper) {
        this.accountClsRepository = accountClsRepository;
        this.accountClsSelectMapper = accountClsSelectMapper;
    }

    /**
     * {@code POST  /AccountCls} : Create a new accountCls.
     *
     * @param accountCls the AccountCls to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new AccountCls, or with
     * status {@code 400 (Bad Request)} if the AccountCls has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(description = "新增会计科目")
    @PostMapping("/account-cls")
//    @PreAuthorize("hasAuthority('acct:AccountCls:add')")
    public AccountCls createAccountCls(@RequestBody AccountCls accountCls) throws URISyntaxException {

        ExampleMatcher matcher = ExampleMatcher.matching();
        final AccountCls filterObj = new AccountCls();
        if (Objects.isNull(accountCls.getId())) {
            final Example<AccountCls> of = Example.of(filterObj, matcher);
            final long count = accountClsRepository.count(of);
        }

        if (Objects.isNull(accountCls.getParentId())) {
            accountCls.setParentId(0L);
        }

        return accountClsRepository.save(accountCls);
    }

    /**
     * {@code GET  /AccountCls} : get all the accountCls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of AccountCls in body.
     */
    @Operation(description = "获取所有会计科目")
    @GetMapping("/account-cls")
//    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public HashMap<String, Object> getAllAccountCls(
            Pageable pageable, AccountCls AccountCls) {
        log.debug("REST request to get a page of UiComponents");

        //创建匹配器，即如何使用查询条件
        //构建对象
        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true)
                .withIgnoreNullValues()
                //名字采用“开始匹配”的方式查询
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                //忽略属性：是否关注。因为是基本类型，需要忽略掉
                .withIgnorePaths("id", "createdDate", "lastModifiedDate");

        //创建实例
        Example<AccountCls> ex = Example.of(AccountCls, matcher);
        final List<AccountCls> allAccountClsOrderByOrdernumAsc = accountClsRepository.findAll(ex);
        final List<AccountClsVO> collect = allAccountClsOrderByOrdernumAsc.stream().map(m -> {
            final AccountClsVO AccountClsDTO = new AccountClsVO();
            BeanUtils.copyProperties(m, AccountClsDTO);
            return AccountClsDTO;
        }).collect(Collectors.toList());
        final GenericTreeBuilderUtil<AccountClsVO> AccountClsDTOGenericTreeBuilderUtil = new GenericTreeBuilderUtil<>(AccountClsVO.class);
        final List<AccountClsVO> AccountClsDTOS = AccountClsDTOGenericTreeBuilderUtil.buildTree(collect);

        //会计科目属性转换成 下划线 再给前端, mapstruct? 直接采用Jackson的JsonNaming注解搞了先
        return new HashMap<>() {{
            put("list", AccountClsDTOS);
            put("total", 0);
        }};

    }

    /**
     * {@code GET  /AccountCls/:id} : get the "id" accountCls.
     *
     * @param id the id of the AccountCls to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the AccountCls, or with status {@code
     * 404 (Not Found)}.
     */
    @GetMapping("/account-cls/{id}")
//    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public AccountCls getAccountCls(@PathVariable Long id) {
        log.debug("REST request to get AccountCls : {}", id);
        Optional<AccountCls> accountCls = accountClsRepository.findById(id);
        return accountCls.get();
    }

    @Operation(description = "删除会计科目")
    @DeleteMapping("/account-cls")
//    @PreAuthorize("hasAuthority('acct:AccountCls:delete')")
    public String deleteAccountCls(@RequestBody List<Long> idList) {
        log.debug("REST request to delete Examples, ids: {}", idList);
        this.accountClsRepository.deleteAllByIdIn(idList);
        return "success";
    }

    @Operation(description = "获取所有一级会计科目")
    @GetMapping("/account-cls/root")
//    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public List<AccountCls> getAllRootAccountCls() {
        log.debug("REST request to get a page of account-cls");

        return accountClsRepository.findAllByParentIdOrderByOrderNumAsc(0L);
    }

    @Operation(description = "获取科目树select")
    @GetMapping("/account-cls/select")
//    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public List<SelectOptionVO> getAccountClsSelectInc() {
        log.debug("REST request to get AccountCls Select");

        final List<AccountCls> accountClsList = accountClsRepository.findAll();
        final List<SelectOptionVO> convert = accountClsSelectMapper.convert(accountClsList);
        final GenericTreeBuilderUtil<SelectOptionVO> genericTreeBuilderUtil = new GenericTreeBuilderUtil<>(SelectOptionVO.class);
        final List<SelectOptionVO> list = genericTreeBuilderUtil.buildTree(convert);
        log.info("左侧树构建: {}", list);
        return list;
    }
}
