package com.z.module.system.web.rest;

import com.z.framework.security.service.TokenProviderService;
import com.z.framework.security.util.SecurityUtils;
import com.z.module.system.domain.User;
import com.z.module.system.repository.UserRepository;
import com.z.module.system.service.LoginLogService;
import com.z.module.system.web.vo.AuthedRespVO;
import com.z.module.system.web.vo.LoginVO;
import com.z.module.system.web.vo.SmsLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.*;

/**
 * @author zhaozhiwei
 * @version V1.0
 * @Title: LoginResource
 * @Package com/longtu/web/rest/LoginResource.java
 * @Description: 登录认证接口
 * @date 2022/7/14 上午11:10
 */
@Tag(name = "登录API")
@RestController
@RequestMapping(value = {"/mobile"})
@Slf4j
public class MobileLoginResource {

    private final UserRepository userRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    private CacheManager cacheManager;

    private final LoginLogService loginLogService;

    private final TokenProviderService tokenProviderService;

    public MobileLoginResource(UserRepository userRepository, PasswordEncoder passwordEncoder, CacheManager cacheManager, LoginLogService loginLogService, TokenProviderService tokenProviderService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
        this.loginLogService = loginLogService;
        this.tokenProviderService = tokenProviderService;
    }

    /**
     * @data: 2022/7/14-上午11:32
     * @User: zhaozhiwei
     * @method: login
     * @return: com.longtu.web.vm.ResponseData<java.lang.String>
     * @Description: 描述
     */
    @Operation(description = "用户密码登录认证")
    @PostMapping("/login")
    public AuthedRespVO loginUserNamePassword(@Valid @RequestBody LoginVO loginVM, HttpServletRequest request) {

        final AuthedRespVO authedRespVO = new AuthedRespVO();
        authedRespVO.setUsername(loginVM.getUsername());

        try {
            String username = loginVM.getUsername();
            String password = loginVM.getPassword();
            final User dbUser = userRepository.findOneByLogin(username).orElse(new User());
            String dbPassWord = dbUser.getPassword();
            if (bCryptPasswordEncoder.matches(password, dbPassWord)) {
                String token = tokenProviderService.generateToken(username, loginVM.isRememberMe(), dbUser.getTenantId());
                authedRespVO.setPermissions(Collections.singletonList("*.*.*"));
                authedRespVO.setToken(token);

                // 登录成功记录日志
                loginLogService.save(loginVM, request);
                return authedRespVO;
            }else{
                log.error("用户 {} 密码 {}不匹配", username, password);
                throw new RuntimeException("用户名或密码错误");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("登录出错", e);
            throw new RuntimeException("登录出错");
        }
    }

    @Operation(description = "退出")
    @GetMapping("loginOut")
    public String loginOut(){
        // 销毁token, 防止下次使用
        final Cache tokenBlackCache = cacheManager.getCache("tokenBlackCache");
        List<String> cacheBlockList;
        if(Objects.isNull(tokenBlackCache.get("tokenBlock"))){
            cacheBlockList = new ArrayList<>();
        }else{
            cacheBlockList = (List<String>) tokenBlackCache.get("tokenBlock").get();
        }
        cacheBlockList.add(SecurityUtils.getTokenId());
        tokenBlackCache.put("tokenBlack", cacheBlockList);

        return "success";
    }

    @Operation(description = "验证码登录认证")
    @PostMapping("/login/sms")
    public AuthedRespVO loginUserSms(@Valid @RequestBody SmsLoginVO smsLoginVO, HttpServletRequest request) {
        // 1. 根据手机号获取用户信息
        Optional<User> oneByPhoneNumber = userRepository.findOneByPhoneNumber(smsLoginVO.getMobile());
        if(oneByPhoneNumber.isPresent()){
            User user = oneByPhoneNumber.get();
            // 2. 登录认证
            final AuthedRespVO authedRespVO = new AuthedRespVO();
            authedRespVO.setUsername(user.getLogin());

            try {
                String username = user.getLogin();
                String token = tokenProviderService.generateToken(username, true, user.getTenantId());
                authedRespVO.setPermissions(Collections.singletonList("*.*.*"));
                authedRespVO.setToken(token);

                // 登录成功记录日志
                LoginVO loginVO = new LoginVO();
                loginVO.setUsername(user.getLogin());
                loginVO.setRememberMe(true);
                loginLogService.save(loginVO, request);
                return authedRespVO;
            } catch (Exception e) {
                log.error("登录出错", e);
                throw new RuntimeException("登录出错");
            }
        }else{
            log.warn("手机号未注册: {}", smsLoginVO.getMobile());
            throw new RuntimeException("用户未注册");
        }
    }

    @Operation(description = "手机号一键登录")
    @PostMapping("/login/number")
    public AuthedRespVO loginPhoneNum(@RequestParam String phoneNum, HttpServletRequest request) {

        // 1. 根据手机号获取用户信息
        Optional<User> oneByPhoneNumber = userRepository.findOneByPhoneNumber(phoneNum);
        if(oneByPhoneNumber.isPresent()){
            User user = oneByPhoneNumber.get();
            // 2. 登录认证
            final AuthedRespVO authedRespVO = new AuthedRespVO();
            authedRespVO.setUsername(user.getLogin());

            try {
                String username = user.getLogin();
                String password = user.getPassword();
                final User dbUser = userRepository.findOneByLogin(username).orElse(new User());
                String dbPassWord = dbUser.getPassword();
                if (bCryptPasswordEncoder.matches(password, dbPassWord)) {
                    String token = tokenProviderService.generateToken(username, true, dbUser.getTenantId());
                    authedRespVO.setPermissions(Collections.singletonList("*.*.*"));
                    authedRespVO.setToken(token);

                    // 登录成功记录日志
                    LoginVO loginVO = new LoginVO();
                    loginVO.setUsername(user.getLogin());
                    loginVO.setRememberMe(true);
                    loginLogService.save(loginVO, request);
                    return authedRespVO;
                }else{
                    log.warn("用户 {} 密码不匹配", username);
                    throw new RuntimeException("用户名或密码错误");
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                log.error("登录出错", e);
                throw new RuntimeException("登录出错");
            }
        }else{
            log.error("登录失败, 手机号: {}", phoneNum);
            throw new RuntimeException("用户未注册");
        }
    }
}
