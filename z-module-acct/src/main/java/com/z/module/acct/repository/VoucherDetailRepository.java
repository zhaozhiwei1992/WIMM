package com.z.module.acct.repository;

import com.z.module.acct.domain.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Long> {
    void deleteAllByIdIn(List<Long> idList);
}
