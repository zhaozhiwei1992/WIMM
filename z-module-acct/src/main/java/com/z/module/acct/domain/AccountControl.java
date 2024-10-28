package com.z.module.acct.domain;

import com.z.framework.common.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "acct_ctrl")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountControl extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id; // 控制ID

    private String setYear; // 核算年度

    private String acctSetCode; // 账套编号

    private String acctClsCode; // 预算指标核算科目代码

    private BigDecimal curAmt; // 可用额度

    private BigDecimal lastAmt; // 科目余额

    private BigDecimal drIncAccAmt; // 借方增加累计金额

    private BigDecimal drDecAccAmt; // 借方减少累计金额

    private BigDecimal crIncAccAmt; // 贷方增加累计金额

    private BigDecimal crDecAccAmt; // 贷方减少累计金额
}