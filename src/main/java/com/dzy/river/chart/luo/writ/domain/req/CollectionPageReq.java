package com.dzy.river.chart.luo.writ.domain.req;

import com.dzy.river.chart.luo.writ.common.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * CollectionPageReq
 *
 * @author zhuzhiwei
 * @date 2025/12/18
 */
@Data
public class CollectionPageReq extends PageReq {

    @Schema(description = "名称（收藏夹名称）")
    private String name;

    @Schema(description = "用户ID（内部使用，从登录用户获取）", hidden = true)
    private Long userId;

    @NotNull(message = "内容类型不能为空")
    @Schema(description = "内容类型(image:图片, note:笔记)")
    private String contentType;
}
