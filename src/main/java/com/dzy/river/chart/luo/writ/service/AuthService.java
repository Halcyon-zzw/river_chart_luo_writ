package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.auth.*;

/**
 * 认证服务接口
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
public interface AuthService {

    /**
     * 用户注册（用户名+密码）
     *
     * @param request 注册请求
     * @return 认证响应（包含token）
     */
    AuthResponse register(RegisterRequest request);

    /**
     * 密码登录（支持用户名/手机号/邮箱 + 密码）
     *
     * @param request 登录请求
     * @param ip      请求IP
     * @return 认证响应（包含token）
     */
    AuthResponse passwordLogin(PasswordLoginRequest request, String ip);

    /**
     * 验证码登录（支持手机号/邮箱 + 验证码，首次登录自动注册）
     *
     * @param request 登录请求
     * @param ip      请求IP
     * @return 认证响应（包含token）
     */
    AuthResponse codeLogin(CodeLoginRequest request, String ip);

    /**
     * 刷新访问令牌
     *
     * @param request 刷新请求
     * @return 认证响应（新的token）
     */
    AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * 发送验证码
     *
     * @param request 发送请求
     * @param ip      请求IP
     * @return 是否发送成功
     */
    boolean sendVerificationCode(SendCodeRequest request, String ip);
}
