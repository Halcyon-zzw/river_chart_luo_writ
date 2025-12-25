package com.dzy.river.chart.luo.writ.domain.req;

import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 级联创建内容请求
 * 一次性创建主分类、子分类和内容
 *
 * @author zhuzhiwei
 * @date 2025/12/25
 */
@Data
@Schema(description = "级联创建内容请求")
public class CreateContentWithCategoriesReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "主分类信息不能为空")
    @Valid
    @Schema(description = "主分类信息", required = true)
    private MainCategoryDTO mainCategory;

    @NotNull(message = "子分类信息不能为空")
    @Valid
    @Schema(description = "子分类信息", required = true)
    private SubCategoryDTO subCategory;

    @NotNull(message = "内容信息不能为空")
    @Valid
    @Schema(description = "内容信息", required = true)
    private ContentDTO content;
}
