package com.z.module.acct.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVO {

    private String creditAccount; // 贷方科目

    private String debitAccount; // 借方科目

    private BigDecimal amt; // 金额

    private String remark; // 备注

    private int type; // 记账类型，1:收入账, 2:支出账

}
