package com.z.module.acct.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * AI 记账解析结果(前端预览用).
 * <p>
 * 前端拿到后展示预览, 用户确认/微调后,
 * 用 creditAccount/debitAccount/amt/remark 调 /acct/account 记账.
 * 借贷拆分由后端记账接口完成, 此处只给科目代码与方向无关的语义.
 *
 * @author zhaozhiwei
 */
@Data
public class AiParseResultVO {

    /**
     * 交易类型: income=收入, expense=支出, transfer=转账.
     */
    private String type;

    /**
     * 业务科目代码(如 衣服=004001, 工资=003001). 转账时为源账户.
     */
    private String bizAccountCode;

    /**
     * 业务科目名称(冗余展示).
     */
    private String bizAccountName;

    /**
     * 收付款账户代码(如 微信=001003). 转账时为目标账户.
     */
    private String payAccountCode;

    /**
     * 收付款账户名称(冗余展示).
     */
    private String payAccountName;

    /**
     * 金额.
     */
    private BigDecimal amt;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 是否启用了兜底(true=某项解析不出走了默认). 前端可据此提示用户核对.
     */
    private boolean fallback;

    /**
     * 供前端直接调记账接口用的借方科目代码(由 type + 科目方向推导).
     */
    private String debitAccount;

    /**
     * 供前端直接调记账接口用的贷方科目代码.
     */
    private String creditAccount;
}
