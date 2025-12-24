package com.dzy.river.chart.luo.writ.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 子分类批量关联标签请求
 *
 * @author zhuzhiwei
 * @date 2025/12/24
 */
@Data
@Schema(description = "子分类批量关联标签请求")
public class BatchLinkSubCategoryTagReq {

    @NotNull(message = "子分类ID不能为空")
    @Schema(description = "子分类ID", required = true)
    private Long subCategoryId;

    @NotEmpty(message = "标签ID列表不能为空")
    @Schema(description = "标签ID列表", required = true)
    private List<Long> tagIds;
}
