package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.dto.auth.*;
import com.dzy.river.chart.luo.writ.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户注册、登录、令牌刷新等认证相关接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "通过用户名和密码注册新账号")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册请求：username={}", request.getUsername());
        AuthResponse response = authService.register(request);
        return Result.success(response);
    }

    @PostMapping("/login/password")
    @Operation(summary = "密码登录", description = "支持用户名/手机号/邮箱 + 密码登录")
    public Result<AuthResponse> passwordLogin(
            @Valid @RequestBody PasswordLoginRequest request,
            HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        log.info("密码登录请求：account={}, ip={}", request.getAccount(), ip);
        AuthResponse response = authService.passwordLogin(request, ip);
        return Result.success(response);
    }

    @PostMapping("/login/code")
    @Operation(summary = "验证码登录", description = "支持手机号/邮箱 + 验证码登录，首次登录自动注册")
    public Result<AuthResponse> codeLogin(
            @Valid @RequestBody CodeLoginRequest request,
            HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        log.info("验证码登录请求：account={}, ip={}", request.getAccount(), ip);
        AuthResponse response = authService.codeLogin(request, ip);
        return Result.success(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用refresh token获取新的access token")
    public Result<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("刷新令牌请求");
        AuthResponse response = authService.refreshToken(request);
        return Result.success(response);
    }

    @PostMapping("/send-code")
    @Operation(summary = "发送验证码", description = "发送短信或邮件验证码")
    public Result<Boolean> sendCode(
            @Valid @RequestBody SendCodeRequest request,
            HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        log.info("发送验证码请求：target={}, type={}, purpose={}, ip={}",
                request.getTarget(), request.getType(), request.getPurpose(), ip);
        boolean success = authService.sendVerificationCode(request, ip);
        return Result.success(success);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多个代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
