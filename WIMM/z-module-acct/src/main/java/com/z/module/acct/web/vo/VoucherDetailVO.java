package com.z.module.acct.web.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.z.module.acct.domain.AccountCls;
import lombok.Data;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class VoucherDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty("凭证号")
    private String voucherNo; // 核算凭证号

    @ExcelProperty("科目编码")
    @Trans(type = TransType.SIMPLE, target = AccountCls.class, fields = "name", ref = "acctClsName")
    private String acctClsCode; // 预算指标核算科目代码，自动翻译为科目名称

    @ExcelProperty("科目名称")
    private String acctClsName; // 翻译后的科目名称（由easy-trans自动填充）

    @ExcelProperty("金额")
    private BigDecimal amt; // 金额

    @ExcelProperty("借贷方向")
    private Integer drCr; // 借贷方向

    @ExcelProperty("备注")
    private String remark; // 备注

    @ExcelProperty("创建日期")
    private String createdDate;
}