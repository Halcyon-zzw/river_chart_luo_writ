package com.dzy.river.chart.luo.writ.domain.req;

import com.dzy.river.chart.luo.writ.common.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 浏览历史分页查询请求
 *
 * @author zhuzhiwei
 * @date 2025/12/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "浏览历史分页查询请求")
public class BrowseHistoryPageReq extends PageReq {

    @Schema(description = "用户ID（查询某个用户的浏览历史）", example = "1")
    private Long userId;

    @Schema(description = "内容标题（支持模糊查询）", example = "Spring Boot")
    private String contentTitle;

    @Schema(description = "内容类型（image:图片, note:笔记）", example = "image")
    private String contentType;

    @Schema(description = "开始时间（最后浏览时间范围-起）", example = "2025-01-01T00:00:00")
    private LocalDateTime startTime;

    @Schema(description = "结束时间（最后浏览时间范围-止）", example = "2025-12-31T23:59:59")
    private LocalDateTime endTime;

    @Schema(description = "内容ID列表（内部使用，用于根据标题过滤后的ID列表）", hidden = true)
    private List<Long> contentIds;
}
