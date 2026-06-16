package com.z.module.acct.service;

import com.z.framework.common.domain.TenantConstants;
import com.z.framework.common.service.TenantCleanup;
import com.z.module.acct.repository.AccountClsRepository;
import com.z.module.acct.repository.VoucherDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 记账模块的租户清理器: 家庭最后一个用户被删除后, 清理其科目与凭证.
 * <p>
 * 与 AcctTenantInitializer 对称, 由系统模块删除用户时通过 SPI 调用.
 * 模板租户({@link TenantConstants#TEMPLATE_TENANT_ID})受保护, 不会被清理.
 *
 * @author zhaozhiwei
 */
@Component
@Slf4j
@Order(10)
public class AcctTenantCleanup implements TenantCleanup {

    private final AccountClsRepository accountClsRepository;
    private final VoucherDetailRepository voucherDetailRepository;

    public AcctTenantCleanup(AccountClsRepository accountClsRepository,
                             VoucherDetailRepository voucherDetailRepository) {
        this.accountClsRepository = accountClsRepository;
        this.voucherDetailRepository = voucherDetailRepository;
    }

    @Override
    public void cleanup(String tenantId) {
        // 双保险: 模板租户绝不清理
        if (TenantConstants.TEMPLATE_TENANT_ID.equals(tenantId)) {
            log.warn("拒绝清理模板租户 {}", tenantId);
            return;
        }
        voucherDetailRepository.deleteAllByTenantId(tenantId);
        accountClsRepository.deleteAllByTenantId(tenantId);
        log.info("租户 {} 科目与凭证已清理", tenantId);
    }
}
