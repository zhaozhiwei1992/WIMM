package com.z.module.acct.repository;

import com.z.module.acct.domain.AccountControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountControlRepository extends JpaRepository<AccountControl, Long> {
    void deleteAllByIdIn(List<Long> idList);
}
