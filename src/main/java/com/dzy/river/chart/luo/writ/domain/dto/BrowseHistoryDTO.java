package com.dzy.river.chart.luo.writ.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 浏览历史表 DTO
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "BrowseHistoryDTO", description = "浏览历史表 DTO")
public class BrowseHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "浏览历史ID", example = "1")
    private Long id;

    @Schema(description = "内容ID", example = "1")
    private Long contentId;

    @Schema(description = "内容标题", example = "Spring Boot入门教程")
    private String contentTitle;

    @Schema(description = "内容类型（image:图片, note:笔记）", example = "image")
    private String contentType;

    @Schema(description = "用户ID（可为空）", example = "1")
    private Long userId;

    @Schema(description = "浏览次数", example = "5")
    private Integer browseCount;

    @Schema(description = "最后浏览时间")
    private LocalDateTime lastBrowseTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;
}
