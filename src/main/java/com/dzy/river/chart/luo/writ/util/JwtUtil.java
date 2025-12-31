package com.dzy.river.chart.luo.writ.util;

import com.dzy.river.chart.luo.writ.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和验证JWT Token
 */
@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Token类型
     */
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    /**
     * Claims中的自定义键
     */
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";

    /**
     * 获取签名密钥
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成Access Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return Access Token
     */
    public String generateAccessToken(Long userId, String username) {
        return generateToken(userId, username, TOKEN_TYPE_ACCESS, jwtProperties.getAccessTokenExpiration());
    }

    /**
     * 生成Refresh Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return Refresh Token
     */
    public String generateRefreshToken(Long userId, String username) {
        return generateToken(userId, username, TOKEN_TYPE_REFRESH, jwtProperties.getRefreshTokenExpiration());
    }

    /**
     * 生成Token（通用方法）
     *
     * @param userId     用户ID
     * @param username   用户名
     * @param tokenType  Token类型
     * @param expiration 过期时间（秒）
     * @return JWT Token
     */
    private String generateToken(Long userId, String username, String tokenType, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_USERNAME, username);
        claims.put(CLAIM_TOKEN_TYPE, tokenType);

        return Jwts.builder()
                .claims(claims)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * 解析Token获取Claims
     *
     * @param token JWT Token
     * @return Claims
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }
            // 检查是否过期
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证Token类型
     *
     * @param token     JWT Token
     * @param tokenType 期望的Token类型
     * @return 是否匹配
     */
    public boolean validateTokenType(String token, String tokenType) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return false;
        }
        String actualType = claims.get(CLAIM_TOKEN_TYPE, String.class);
        return tokenType.equals(actualType);
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        Object userIdObj = claims.get(CLAIM_USER_ID);
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        return (Long) userIdObj;
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get(CLAIM_USERNAME, String.class);
    }

    /**
     * 检查Token是否即将过期（剩余时间少于1小时）
     *
     * @param token JWT Token
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        if (expiration == null) {
            return true;
        }
        long remainingTime = expiration.getTime() - System.currentTimeMillis();
        // 剩余时间少于1小时（3600000毫秒）
        return remainingTime < 3600000;
    }
}
