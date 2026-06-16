package com.z.module.acct.web.vo;

import lombok.Data;

/**
 * AI 记账解析请求.
 *
 * @author zhaozhiwei
 */
@Data
public class AiParseRequest {

    /**
     * 用户输入的自然语言记账描述, 如 "买衣服花了200, 微信付的".
     */
    private String text;
}
