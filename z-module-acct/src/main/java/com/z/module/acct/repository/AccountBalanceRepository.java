package com.z.module.acct.repository;

import com.z.module.acct.domain.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
    void deleteAllByIdIn(List<Long> idList);
}
