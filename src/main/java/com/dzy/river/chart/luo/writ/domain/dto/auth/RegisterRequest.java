package com.dzy.river.chart.luo.writ.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求DTO
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@Schema(name = "RegisterRequest", description = "用户注册请求")
public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名", example = "zhangsan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 64, message = "用户名长度必须在3-64个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @Schema(description = "密码", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在6-32个字符之间")
    private String password;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "昵称", example = "张三")
    private String nickname;
}
