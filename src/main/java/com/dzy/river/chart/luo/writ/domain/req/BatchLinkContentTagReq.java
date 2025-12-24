package com.dzy.river.chart.luo.writ.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 内容批量关联标签请求
 *
 * @author zhuzhiwei
 * @date 2025/12/24
 */
@Data
@Schema(description = "内容批量关联标签请求")
public class BatchLinkContentTagReq {

    @NotNull(message = "内容ID不能为空")
    @Schema(description = "内容ID", required = true)
    private Long contentId;

    @NotEmpty(message = "标签ID列表不能为空")
    @Schema(description = "标签ID列表", required = true)
    private List<Long> tagIds;
}
