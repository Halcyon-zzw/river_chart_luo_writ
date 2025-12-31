package com.dzy.river.chart.luo.writ.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 验证码登录请求DTO
 * 支持手机号、邮箱 + 验证码登录
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@Schema(name = "CodeLoginRequest", description = "验证码登录请求")
public class CodeLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "账号（手机号/邮箱）", example = "13800138000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "验证码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码不能为空")
    private String code;
}
