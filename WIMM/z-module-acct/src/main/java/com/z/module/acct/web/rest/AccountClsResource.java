package com.z.module.acct.web.rest;

import com.z.framework.common.web.vo.SelectOptionVO;
import com.z.module.acct.domain.AccountCls;
import com.z.module.acct.service.AccountClsService;
import com.z.module.acct.web.vo.AccountClsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: AccountClsResource
 * @Package com/z/module/acct/web/rest/AccountClsResource.java
 * @Description: 会计科目管理接口
 * @date 2024/10/28 16:53
 */
@Tag(name = "会计科目API")
@RestController
@RequestMapping("/acct")
@Slf4j
public class AccountClsResource {

    private final AccountClsService accountClsService;

    public AccountClsResource(AccountClsService accountClsService) {
        this.accountClsService = accountClsService;
    }

    @Operation(description = "新增会计科目")
    @PostMapping("/account-cls")
    @PreAuthorize("hasAuthority('acct:AccountCls:add')")
    public AccountCls createAccountCls(@RequestBody AccountCls accountCls) throws URISyntaxException {
        return accountClsService.createAccountCls(accountCls);
    }

    @Operation(description = "获取所有会计科目")
    @GetMapping("/account-cls")
    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public HashMap<String, Object> getAllAccountCls(Pageable pageable, AccountCls accountCls) {
        List<AccountClsVO> tree = accountClsService.getAllAccountClsTree(accountCls);
        return new HashMap<>() {{
            put("list", tree);
            put("total", 0);
        }};
    }

    @GetMapping("/account-cls/{id}")
    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public AccountCls getAccountCls(@PathVariable Long id) {
        return accountClsService.getAccountCls(id);
    }

    @Operation(description = "删除会计科目")
    @DeleteMapping("/account-cls")
    @PreAuthorize("hasAuthority('acct:AccountCls:delete')")
    public String deleteAccountCls(@RequestBody List<Long> idList) {
        accountClsService.deleteAccountCls(idList);
        return "success";
    }

    @Operation(description = "获取所有一级会计科目")
    @GetMapping("/account-cls/root")
    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public List<AccountCls> getAllRootAccountCls() {
        return accountClsService.getAllRootAccountCls();
    }

    @Operation(description = "获取科目树select")
    @GetMapping("/account-cls/select")
    @PreAuthorize("hasAuthority('acct:AccountCls:view')")
    public List<SelectOptionVO> getAccountClsSelect() {
        return accountClsService.getAccountClsSelect();
    }
}
