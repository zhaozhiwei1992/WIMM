package com.z.module.acct.domain;

import com.z.framework.common.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "acct_bal")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountBalance extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 余额ID

    private String setYear; // 核算年度

    private String acctClsCode; // 预算指标核算科目代码

    private BigDecimal debitBal; // 借方余额

    private BigDecimal creditBal; // 贷方余额

    private BigDecimal debitHappenAmt; // 借方发生额

    private BigDecimal creditHappenAmt; // 贷方发生额
}