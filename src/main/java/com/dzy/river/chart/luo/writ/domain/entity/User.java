package com.dzy.river.chart.luo.writ.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@Schema(name = "User", description = "用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，唯一
     */
    @TableField("username")
    private String username;

    /**
     * 邮箱，唯一
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 加密后的密码
     */
    @TableField("password_hash")
    private String passwordHash;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 微信openid，唯一
     */
    @TableField("openid")
    private String openid;

    /**
     * 微信unionid，唯一
     */
    @TableField("unionid")
    private String unionid;

    /**
     * 微信用户信息，JSON格式
     */
    @TableField("wechat_info")
    private String wechatInfo;

    /**
     * 状态：0-禁用，1-启用，2-锁定
     */
    @TableField("status")
    private Byte status;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;

    /**
     * 角色：SUPER_ADMIN-超级管理员, ADMIN-管理员, USER-普通用户
     */
    @TableField("role")
    private String role;

    /**
     * 额外权限配置，JSON格式
     */
    @TableField("permissions")
    private String permissions;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 密码最后修改时间
     */
    @TableField("password_update_time")
    private LocalDateTime passwordUpdateTime;

    /**
     * 账号过期时间
     */
    @TableField("account_expire_time")
    private LocalDateTime accountExpireTime;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}