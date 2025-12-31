package com.dzy.river.chart.luo.writ.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 发送验证码请求DTO
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@Schema(name = "SendCodeRequest", description = "发送验证码请求")
public class SendCodeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "目标（手机号/邮箱）", example = "13800138000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "目标不能为空")
    private String target;

    @Schema(description = "类型：PHONE_SMS-手机短信，EMAIL-邮件", example = "PHONE_SMS", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "类型不能为空")
    private String type;

    @Schema(description = "用途：REGISTER-注册，LOGIN-登录，RESET_PASSWORD-重置密码，BIND-绑定", example = "LOGIN", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用途不能为空")
    private String purpose;
}
