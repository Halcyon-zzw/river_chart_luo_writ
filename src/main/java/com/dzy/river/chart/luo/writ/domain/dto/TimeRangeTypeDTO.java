package com.dzy.river.chart.luo.writ.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 时间范围类型DTO
 *
 * @author zhuzhiwei
 * @date 2026/1/16 13:56
 */
@Data
public class TimeRangeTypeDTO {

    /**
     * @see com.dzy.river.chart.luo.writ.domain.enums.TimeRangeTypeEnum
     */
    @Schema(description = "时间范围类型编码")
    private String code;

    @Schema(description = "时间范围类型描述")
    private String desc;
}
