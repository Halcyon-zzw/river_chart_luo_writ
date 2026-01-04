package com.dzy.river.chart.luo.writ.interceptor;

import com.dzy.river.chart.luo.writ.context.UserContext;
import com.dzy.river.chart.luo.writ.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT认证拦截器
 * 从请求头中解析JWT令牌，提取用户ID并存储到ThreadLocal和请求头中
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Slf4j
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_ID_HEADER = "x-user-id";
    private static final Long DEFAULT_USER_ID = 1L;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 请求处理前执行
     * 解析JWT令牌，提取用户ID并设置到ThreadLocal和请求头
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        log.debug("JWT拦截器执行 - 请求路径: {} {}", request.getMethod(), requestURI);

        // 1. 获取Authorization请求头
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        Long userId = DEFAULT_USER_ID;

        if (authorizationHeader != null && !authorizationHeader.trim().isEmpty()) {
            // 2. 提取JWT令牌（格式：Bearer <token>）
            String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

            if (token != null) {
                // 3. 解析JWT令牌，提取用户ID
                Long extractedUserId = jwtUtil.getUserIdFromToken(token);

                if (extractedUserId != null) {
                    userId = extractedUserId;
                    log.info("JWT认证成功 - userId: {}", userId);
                } else {
                    log.warn("JWT令牌解析失败，无法提取userId，使用默认值: {}", DEFAULT_USER_ID);
                }
            } else {
                log.warn("Authorization请求头格式错误，使用默认userId: {}", DEFAULT_USER_ID);
            }
        } else {
            log.info("未找到Authorization请求头，使用默认userId: {} (匿名访问)", DEFAULT_USER_ID);
        }

        // 4. 设置用户ID到ThreadLocal
        UserContext.setUserId(userId);

        // 5. 设置用户ID到请求头（兼容旧代码）
        request.setAttribute(USER_ID_HEADER, userId);

        log.debug("JWT拦截器完成 - userId已设置为: {}", userId);

        // 继续执行后续处理器
        return true;
    }

    /**
     * 请求处理完成后执行
     * 清理ThreadLocal，防止内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理ThreadLocal
        UserContext.clear();
        log.debug("JWT拦截器清理 - ThreadLocal已清理，请求路径: {}", request.getRequestURI());
    }
}
