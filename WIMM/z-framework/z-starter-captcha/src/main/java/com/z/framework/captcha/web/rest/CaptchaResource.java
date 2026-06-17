package com.z.framework.captcha.web.rest;

import com.z.framework.captcha.service.CaptchaTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.code.kaptcha.Producer;

/**
 * @Title: CaptchaController
 * @Package com/longtu/web/controller/CaptchaController.java
 * @Description: 获取验证码。无状态方案：返回 {img, captchaToken}，验证码答案以 HMAC 签名的
 * token 下发，前端登录时回传，后端验签比对，不再依赖 session（跨域/跨端口/反代均可）。
 * 前端通过 response.img 直接展示 base64 图片。
 * @author zhaozhiwei
 * @date 2022/8/31 上午9:35
 * @version V1.0
 */
@RestController
@Slf4j
public class CaptchaResource {

    private final Producer captchaProducer;

    private final CaptchaTokenService captchaTokenService;

    public CaptchaResource(Producer captchaProducer, CaptchaTokenService captchaTokenService) {
        this.captchaProducer = captchaProducer;
        this.captchaTokenService = captchaTokenService;
    }

    /**
     * @data: 2022/8/31-上午10:09
     * @User: zhaozhiwei
     * @method: getCaptchaCode
     * @Description: 数字验证码，返回 {img: base64图片, captchaToken: 验证码签名token}
     * 前端登录时把 captchaToken 连同用户输入的验证码一起提交到 /api/system/login
     */
    @GetMapping("/captcha/numCode")
    public Map<String, String> getCaptchaCode() throws IOException {
        // 生成验证码文本
        String captchaText = captchaProducer.createText();
        // 创建验证码图片
        BufferedImage bi = captchaProducer.createImage(captchaText);

        // 将验证码图片转换为 base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", outputStream);
        byte[] captchaBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(captchaBytes);

        // 签发无状态 token（替代 session 存验证码）
        String captchaToken = captchaTokenService.generate(captchaText);

        Map<String, String> result = new HashMap<>();
        result.put("img", base64Image);
        result.put("captchaToken", captchaToken);
        return result;
    }
}
