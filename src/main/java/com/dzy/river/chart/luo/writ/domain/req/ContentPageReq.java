package com.dzy.river.chart.luo.writ.domain.req;

import com.dzy.river.chart.luo.writ.common.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ContentategoryPageReq
 *
 * @author zhuzhiwei
 * @date 2025/12/18 15:55
 */
@Data
public class ContentPageReq extends PageReq {

    @NotNull(message = "子分类ID不能为空")
    @Schema(description = "子分类ID")
    private Long subCategoryId;

    @Schema(description = "内容类型(image:图片, note:笔记)")
    private String contentType;
}
