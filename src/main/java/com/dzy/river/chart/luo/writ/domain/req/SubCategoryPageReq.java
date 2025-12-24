package com.dzy.river.chart.luo.writ.domain.req;

import com.dzy.river.chart.luo.writ.common.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SubCategoryPageReq
 *
 * @author zhuzhiwei
 * @date 2025/12/18 15:55
 */
@Data
public class SubCategoryPageReq extends PageReq {

    @Schema(description = "主分类ID")
    private Long mainCategoryId;


    @Schema(description = "子分类名称")
    private String name;
}
