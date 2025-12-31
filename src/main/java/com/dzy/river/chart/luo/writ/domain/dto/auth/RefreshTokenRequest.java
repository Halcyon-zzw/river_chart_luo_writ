package com.dzy.river.chart.luo.writ.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 刷新令牌请求DTO
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@Schema(name = "RefreshTokenRequest", description = "刷新令牌请求")
public class RefreshTokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
