package com.dzy.river.chart.luo.writ.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 用户表 DTO
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserDTO", description = "用户表 DTO")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID，主键", example = "1")
    private Long id;

    @Schema(description = "用户名，唯一")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 64, message = "用户名长度必须在3-64个字符之间")
    private String username;

    @Schema(description = "邮箱，唯一")
//    @NotBlank(message = "邮箱不能为空")
//    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "加密后的密码")
    private String passwordHash;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "微信openid，唯一")
    private String openid;

    @Schema(description = "微信unionid，唯一")
    private String unionid;

    @Schema(description = "微信用户信息，JSON格式")
    private String wechatInfo;

    @Schema(description = "状态：0-禁用，1-启用，2-锁定")
    private Byte status;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "角色：SUPER_ADMIN-超级管理员, ADMIN-管理员, USER-普通用户")
    private String role;

    @Schema(description = "额外权限配置，JSON格式")
    private String permissions;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "密码最后修改时间")
    private LocalDateTime passwordUpdateTime;

    @Schema(description = "账号过期时间")
    private LocalDateTime accountExpireTime;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}