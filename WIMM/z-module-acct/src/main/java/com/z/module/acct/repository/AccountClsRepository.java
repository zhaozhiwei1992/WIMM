package com.z.module.acct.repository;

import com.z.module.acct.domain.AccountCls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AccountClsRepository extends JpaRepository<AccountCls, Long> {

    // ===== 按租户(家庭)隔离的查询: 多租户下只允许查询本家庭科目 =====

    List<AccountCls> findAllByTenantIdOrderByOrderNumAsc(String tenantId);

    List<AccountCls> findAllByTenantIdAndParentIdOrderByOrderNumAsc(String tenantId, Long parentId);

    List<AccountCls> findAllByTenantIdAndCodeIn(String tenantId, Collection<String> codes);

    List<AccountCls> findAllByTenantIdAndParentIdIn(String tenantId, Collection<Long> parentIds);

    boolean existsByTenantIdAndId(String tenantId, Long id);

    /**
     * 删除某家庭的全部科目(用于家庭清空时的资源回收).
     */
    void deleteAllByTenantId(String tenantId);
}
