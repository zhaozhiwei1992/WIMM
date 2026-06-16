package com.z.module.acct.service;

import com.z.framework.common.service.TenantInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 记账模块的租户初始化器: 新家庭注册时为其复制一份预设会计科目.
 * <p>
 * 由 z-module-system 的注册流程通过 TenantInitializer SPI 调用,
 * 避免 system 模块反向依赖 acct 模块.
 *
 * @author zhaozhiwei
 */
@Component
@Slf4j
@Order(10)
public class AcctTenantInitializer implements TenantInitializer {

    private final AccountClsService accountClsService;

    public AcctTenantInitializer(AccountClsService accountClsService) {
        this.accountClsService = accountClsService;
    }

    @Override
    public void initialize(String tenantId) {
        accountClsService.initPresetForTenant(tenantId);
    }
}
