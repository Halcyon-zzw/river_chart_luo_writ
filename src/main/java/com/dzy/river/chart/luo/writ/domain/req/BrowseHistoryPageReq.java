package com.dzy.river.chart.luo.writ.domain.req;

import com.dzy.river.chart.luo.writ.common.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @Schema(description = "内容ID（查询某个内容的浏览记录）", example = "1")
    private Long contentId;
}
