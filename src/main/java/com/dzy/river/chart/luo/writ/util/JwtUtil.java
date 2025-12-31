package com.dzy.river.chart.luo.writ.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 - 基于Hutool实现
 * 用于生成和解析JWT令牌
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Slf4j
@Component
public class JwtUtil {

    /**
     * JWT密钥（从配置文件读取，如果没有配置则使用默认值）
     */
    @Value("${jwt.secret:river-chart-luo-writ-secret-key-2025}")
    private String secret;

    /**
     * Access Token过期时间（默认2小时，单位：秒）
     */
    @Value("${jwt.access-token-expire:7200}")
    private Long accessTokenExpire;

    /**
     * Refresh Token过期时间（默认7天，单位：秒）
     */
    @Value("${jwt.refresh-token-expire:604800}")
    private Long refreshTokenExpire;

    /**
     * 生成Access Token
     *
     * @param userId 用户ID
     * @return JWT令牌
     */
    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessTokenExpire, "access");
    }

    /**
     * 生成Refresh Token
     *
     * @param userId 用户ID
     * @return JWT令牌
     */
    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenExpire, "refresh");
    }

    /**
     * 生成JWT令牌
     *
     * @param userId 用户ID
     * @param expireSeconds 过期时间（秒）
     * @param tokenType 令牌类型（access/refresh）
     * @return JWT令牌
     */
    private String generateToken(Long userId, Long expireSeconds, String tokenType) {
        Date now = new Date();
        Date expireTime = DateUtil.offsetSecond(now, expireSeconds.intValue());

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("type", tokenType);
        payload.put("iat", now.getTime() / 1000);  // 签发时间（秒）
        payload.put("exp", expireTime.getTime() / 1000);  // 过期时间（秒）

        return JWTUtil.createToken(payload, secret.getBytes());
    }

    /**
     * 验证并解析JWT令牌
     *
     * @param token JWT令牌
     * @return JWT对象，验证失败返回null
     */
    public JWT parseToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            // 验证签名
            boolean verify = jwt.setKey(secret.getBytes()).verify();
            if (!verify) {
                log.warn("JWT token signature verification failed");
                return null;
            }
            // 验证过期时间
            Date expireTime = jwt.getPayload("exp") != null
                    ? new Date(Long.parseLong(jwt.getPayload("exp").toString()) * 1000)
                    : null;
            if (expireTime != null && expireTime.before(new Date())) {
                log.warn("JWT token has expired");
                return null;
            }
            return jwt;
        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从JWT令牌中提取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID，解析失败返回null
     */
    public Long getUserIdFromToken(String token) {
        JWT jwt = parseToken(token);
        if (jwt == null) {
            return null;
        }
        try {
            Object userIdObj = jwt.getPayload("userId");
            if (userIdObj == null) {
                log.warn("UserId not found in JWT token");
                return null;
            }
            // 处理不同类型的userId（Integer、Long、String）
            if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                return Long.parseLong((String) userIdObj);
            }
            log.warn("Invalid userId type in JWT token: {}", userIdObj.getClass());
            return null;
        } catch (Exception e) {
            log.error("Failed to extract userId from JWT token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Authorization请求头中提取JWT令牌
     * 格式：Bearer <token>
     *
     * @param authorizationHeader Authorization请求头的值
     * @return JWT令牌，如果格式不正确返回null
     */
    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            return null;
        }
        // 检查是否以 "Bearer " 开头（注意有空格）
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        log.warn("Invalid Authorization header format: {}", authorizationHeader);
        return null;
    }
}
