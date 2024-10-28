package com.z.module.acct.domain;

import com.z.framework.common.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "acct_vou_detail")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherDetail extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id; // 凭证分录唯一标识

    private String voucherNo; // 核算凭证号

    private String acctClsCode; // 预算指标核算科目代码

    private String acctClsName; // 预算指标核算科目名称

    private BigDecimal amt; // 金额

    private Integer drCr; // 借贷方向

    private String remark; // 备注
}