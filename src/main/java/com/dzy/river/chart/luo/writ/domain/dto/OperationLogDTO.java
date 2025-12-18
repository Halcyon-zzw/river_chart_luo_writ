package com.dzy.river.chart.luo.writ.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 操作日志表 DTO
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "OperationLogDTO", description = "操作日志表 DTO")
public class OperationLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志ID", example = "1")
    private Long id;

    @Schema(description = "操作用户ID（逻辑关联user.id）")
    private Long userId;

    @Schema(description = "操作类型：CREATE, UPDATE, DELETE, VIEW, LOGIN, etc.")
    private String operationType;

    @Schema(description = "操作目标表")
    private String targetTable;

    @Schema(description = "操作目标ID")
    private String targetId;

    @Schema(description = "操作描述")
    private String operationDescription;

    @Schema(description = "旧值（JSON格式）")
    private String oldValues;

    @Schema(description = "新值（JSON格式）")
    private String newValues;

    @Schema(description = "操作IP地址")
    private String ipAddress;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

}