package com.z.module.acct.repository;

import com.z.module.acct.domain.AccountCls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountClsRepository extends JpaRepository<AccountCls, Long> {
    void deleteAllByIdIn(List<Long> idList);

    List<AccountCls> findAllByParentIdOrderByOrderNumAsc(long l);

    List<AccountCls> findAllByCodeIn(List<String> list);
}
