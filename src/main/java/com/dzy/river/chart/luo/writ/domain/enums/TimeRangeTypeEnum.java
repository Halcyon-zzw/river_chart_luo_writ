package com.dzy.river.chart.luo.writ.domain.enums;

import lombok.Getter;

/**
 * 时间类型枚举
 *
 * @author zhuzhiwei
 * @date 2026/1/16 10:45
 */
@Getter
public enum TimeRangeTypeEnum {

    ALL("all", "所有"),

    TODAY("today", "今天"),
    /**
     * 三天内
     */
    THREE_DAYS("threeDays", "三天内"),

    SEVEN_DAYS("sevenDays", "七天内"),

    ONE_MONTH("oneMonth", "一个月内"),

    ;

    private final String code;

    private final String desc;


    TimeRangeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TimeRangeTypeEnum getByCode(String code) {
        for (TimeRangeTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
