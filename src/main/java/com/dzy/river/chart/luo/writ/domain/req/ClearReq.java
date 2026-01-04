package com.dzy.river.chart.luo.writ.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * clearReq
 *
 * @author zhuzhiwei
 * @date 2026/1/4 15:49
 */
@Data
public class ClearReq {

    /**
     * @see com.dzy.river.chart.luo.writ.domain.enums.ContentTypeEnum
     */
    @Schema(description = "内容类型")
    private String contentType;
}
