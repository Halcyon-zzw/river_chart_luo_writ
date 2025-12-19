package com.dzy.river.chart.luo.writ.domain.enums;

import lombok.Getter;

/**
 * 图片类型枚举
 *
 * @author zhuzhiwei
 * @date 2025/12/19 09:55
 */
@Getter
public enum ContentTypeEnum {

    IMAGE("image", "图片"),

    NOTE("note", "笔记");


    private final String code;

    private final String desc;

    ContentTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
