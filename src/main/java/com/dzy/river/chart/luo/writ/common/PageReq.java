package com.dzy.river.chart.luo.writ.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

/**
 * 分页请求
 *
 * @author zhuzhiwei
 * @date 2025/12/18 14:16
 */
@Data
@Schema(description = "分页请求参数")
public class PageReq {
    @Schema(description = "当前第几页，默认1页开始", example = "1")
    private Integer pageNum;

    @Schema(description = "每页数量", example = "20")
    private Integer pageSize;

    public <T> Page<T> convertToPage() {
        Page<T> tPage = new Page<>();
        tPage.setCurrent(Optional.ofNullable(pageNum).orElse(1));
        tPage.setSize(Optional.ofNullable(pageSize).orElse(20));
        return tPage;
    }
}
