package com.z.module.acct.repository;

import com.z.module.acct.domain.VoucherDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Long> {

    // ===== 按租户(家庭)隔离的查询 =====

    List<VoucherDetail> findAllByTenantIdAndAcctClsCodeIn(String tenantId, Collection<String> codes);

    List<VoucherDetail> findAllByTenantIdOrderByCreatedDateDesc(String tenantId, Sort sort);

    void deleteAllByVoucherNoInAndTenantId(Collection<String> voucherNos, String tenantId);
}
