package com.z.framework.captcha.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 无状态图形验证码 token 服务（替代 session 存验证码）
 *
 * <p>原理：验证码答案 + 过期时间戳经 HMAC-SHA256 签名，生成一个自包含 token 下发前端；
 * 登录时前端回传 token + 用户输入，后端验签 + 比对 + 校验过期。
 *
 * <p>无状态带来的好处：跨域/跨端口/反代/Nginx/集群均无感，不依赖 session、cookie、Redis。
 * HMAC 仅用于防篡改（防止前端篡改 code 或 exp），验证码本身的安全性由图片识别门槛 + 短 TTL 保证。
 *
 * <p>token 结构：base64url(payload).base64url(hmac)，payload 为明文 json：{"code":"ab3d","exp":毫秒时间戳}
 *
 * @author zhaozhiwei
 */
@Component
@Slf4j
public class CaptchaTokenService {

    /**
     * HMAC 密钥。验证码答案本就由图片展示，HMAC 只防篡改、非保密用途，固定密钥可接受。
     * 如需更高强度，可改为从配置读取。
     */
    private static final String SECRET = "wimm-captcha-hmac-secret-2026";

    private static final long EXPIRE_MILLIS = 5 * 60 * 1000L;

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    /**
     * 生成验证码 token
     *
     * @param code 验证码明文（如图里的 "ab3d"）
     * @return 自包含 token，前端获取验证码后保存，登录时回传
     */
    public String generate(String code) {
        long exp = System.currentTimeMillis() + EXPIRE_MILLIS;
        // payload: {"code":"<code>","exp":<exp>}  简单拼接，code 用 base64 避免特殊字符
        String codePart = URL_ENCODER.encodeToString(code.getBytes(StandardCharsets.UTF_8));
        String payload = codePart + "." + exp;
        String hmac = hmac(payload);
        return payload + "." + hmac;
    }

    /**
     * 校验 token 并返回其中保存的验证码明文；验签失败/过期返回 null
     */
    public String validate(String token, String inputCode) {
        if (token == null || inputCode == null) {
            return null;
        }
        String[] parts = token.split("\\.");
        // 期望结构: codeBase64 . exp . hmac
        if (parts.length != 3) {
            return null;
        }
        String codeBase64 = parts[0];
        long exp;
        try {
            exp = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        String hmac = parts[2];

        String payload = codeBase64 + "." + exp;
        String expectedHmac = hmac(payload);
        // 防篡改校验
        if (!MessageDigest.isEqual(
                expectedHmac.getBytes(StandardCharsets.UTF_8),
                hmac.getBytes(StandardCharsets.UTF_8))) {
            return null;
        }
        // 过期校验
        if (System.currentTimeMillis() > exp) {
            return null;
        }
        // 比对用户输入（忽略大小写）
        String code = new String(URL_DECODER.decode(codeBase64), StandardCharsets.UTF_8);
        if (!code.equalsIgnoreCase(inputCode)) {
            return null;
        }
        return code;
    }

    private String hmac(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return URL_ENCODER.encodeToString(raw);
        } catch (Exception e) {
            throw new IllegalStateException("HMAC 签名失败", e);
        }
    }
}
