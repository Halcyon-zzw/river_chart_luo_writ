package com.dzy.river.chart.luo.writ.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 记录浏览请求
 *
 * @author zhuzhiwei
 * @date 2025/12/26
 */
@Data
@Schema(description = "记录浏览请求")
public class RecordBrowseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "内容ID不能为空")
    @Schema(description = "内容ID", required = true, example = "1")
    private Long contentId;

    @Schema(description = "用户ID（可为空，表示匿名浏览）", example = "1")
    private Long userId;
}
