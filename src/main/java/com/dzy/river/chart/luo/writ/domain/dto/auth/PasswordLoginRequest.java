package com.dzy.river.chart.luo.writ.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 密码登录请求DTO
 * 支持用户名、手机号、邮箱 + 密码登录
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@Schema(name = "PasswordLoginRequest", description = "密码登录请求")
public class PasswordLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "账号（用户名/手机号/邮箱）", example = "zhangsan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "密码", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    private String password;
}
