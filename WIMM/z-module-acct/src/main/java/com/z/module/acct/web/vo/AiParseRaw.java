package com.z.module.acct.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * AI 模型直接返回的解析结构(经 BeanOutputConverter 约束的 JSON 反序列化目标).
 * <p>
 * 字段名与 prompt 中要求的 JSON 一一对应. 科目用名称, 由 Service 再匹配到本家庭真实 code.
 *
 * @author zhaozhiwei
 */
@Data
public class AiParseRaw {

    /**
     * 交易类型: income / expense / transfer
     */
    private String type;

    /**
     * 业务科目名称(如 "衣服"/"工资"); 转账时为源账户名称.
     */
    private String bizAccountName;

    /**
     * 收付款账户名称(如 "微信"/"现金"); 转账时为目标账户名称.
     */
    private String payAccountName;

    /**
     * 金额(正数).
     */
    private BigDecimal amt;

    /**
     * 备注(可空).
     */
    private String remark;
}
