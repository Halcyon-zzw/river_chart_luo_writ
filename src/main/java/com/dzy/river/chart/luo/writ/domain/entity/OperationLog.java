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
 * 操作日志表
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("operation_log")
@Schema(name = "OperationLog", description = "操作日志表")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID（逻辑关联user.id）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 操作类型：CREATE, UPDATE, DELETE, VIEW, LOGIN, etc.
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 操作目标表
     */
    @TableField("target_table")
    private String targetTable;

    /**
     * 操作目标ID
     */
    @TableField("target_id")
    private String targetId;

    /**
     * 操作描述
     */
    @TableField("operation_description")
    private String operationDescription;

    /**
     * 旧值（JSON格式）
     */
    @TableField("old_values")
    private String oldValues;

    /**
     * 新值（JSON格式）
     */
    @TableField("new_values")
    private String newValues;

    /**
     * 操作IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}