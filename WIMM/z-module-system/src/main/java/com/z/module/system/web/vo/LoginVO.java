package com.z.module.system.web.vo;

import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@ToString
public class LoginVO {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    private boolean rememberMe;

    @NotNull
    private String captcha;

    /**
     * 验证码签名 token（获取验证码时后端下发，登录时回传）
     * 无状态方案：后端据此验签比对，替代 session 存验证码
     */
    private String captchaToken;

}
