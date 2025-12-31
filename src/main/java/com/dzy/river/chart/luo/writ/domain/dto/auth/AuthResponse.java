package com.dzy.river.chart.luo.writ.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 认证响应DTO
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthResponse", description = "认证响应")
public class AuthResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "访问令牌过期时间（秒）", example = "7200")
    private Long expiresIn;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;
}
