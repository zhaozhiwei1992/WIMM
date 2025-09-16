package com.z.module.acct.domain;

import com.alibaba.excel.annotation.ExcelProperty;
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
    private Long id; // 凭证分录唯一标识

    @ExcelProperty("凭证号")
    private String voucherNo; // 核算凭证号

    @ExcelProperty("科目编码")
    private String acctClsCode; // 预算指标核算科目代码

    @ExcelProperty("科目名称")
    private String acctClsName; // 预算指标核算科目名称

    @ExcelProperty("金额")
    private BigDecimal amt; // 金额

    @ExcelProperty("借贷方向")
    private Integer drCr; // 借贷方向

    @ExcelProperty("备注")
    private String remark; // 备注
}