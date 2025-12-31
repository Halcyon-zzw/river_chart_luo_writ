package com.dzy.river.chart.luo.writ.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码表
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("verification_code")
@Schema(name = "VerificationCode", description = "验证码表")
public class VerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 目标（手机号或邮箱）
     */
    @TableField("target")
    private String target;

    /**
     * 验证码
     */
    @TableField("code")
    private String code;

    /**
     * 类型：PHONE_SMS-手机短信，EMAIL-邮件
     */
    @TableField("type")
    private String type;

    /**
     * 用途：REGISTER-注册，LOGIN-登录，RESET_PASSWORD-重置密码，BIND-绑定
     */
    @TableField("purpose")
    private String purpose;

    /**
     * 请求IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 是否已验证：0-未验证，1-已验证
     */
    @TableField("verified")
    private Byte verified;

    /**
     * 验证时间
     */
    @TableField("verify_time")
    private LocalDateTime verifyTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 验证码类型枚举
     */
    public static class Type {
        public static final String PHONE_SMS = "PHONE_SMS";
        public static final String EMAIL = "EMAIL";
    }

    /**
     * 验证码用途枚举
     */
    public static class Purpose {
        public static final String REGISTER = "REGISTER";
        public static final String LOGIN = "LOGIN";
        public static final String RESET_PASSWORD = "RESET_PASSWORD";
        public static final String BIND = "BIND";
    }
}
