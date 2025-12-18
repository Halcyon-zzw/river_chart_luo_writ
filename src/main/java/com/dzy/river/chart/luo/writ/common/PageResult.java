package com.dzy.river.chart.luo.writ.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class PageResult<T> implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(PageResult.class);
    private static final long serialVersionUID = -1L;

    @Schema(description = "默认分页彩虹展示数量")
    public static final int RAINBOW_NUM = 5;


    @Schema(description = "第几页")
    private Integer pageNo = 1;

    @Schema(description = "第几页")
    private Integer pageNum = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 20;

    @Schema(description = "分页类型")
    private String pageType;

    @Schema(description = "分页类型描述")
    private String pageTypeDesc;

    @Schema(description = "总页数")
    private Integer totalPage = 0;

    @Schema(description = "总记录数")
    private Integer totalRows = 0;

    @Schema(description = "结果集")
    private List<T> rows;

    @Schema(description = "彩虹页")
    private int[] rainbow;

    public PageResult() {
    }



    public PageResult(Page<T> page) {
        this.setRows(page.getRecords());
        this.setTotalRows(Convert.toInt(page.getTotal()));
        this.setPageNo(Convert.toInt(page.getCurrent()));
        this.setPageNum(Convert.toInt(page.getCurrent()));
        this.setPageSize(Convert.toInt(page.getSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(page.getTotal()), Convert.toInt(page.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()), Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }

    public PageResult(IPage<T> page) {
        this.setRows(page.getRecords());
        this.setTotalRows(Convert.toInt(page.getTotal()));
        this.setPageNo(Convert.toInt(page.getCurrent()));
        this.setPageNum(Convert.toInt(page.getCurrent()));
        this.setPageSize(Convert.toInt(page.getSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(page.getTotal()), Convert.toInt(page.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()), Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }

    public PageResult(Page<?> page, List<T> t) {
        this.setRows(t);
        this.setTotalRows(Convert.toInt(page.getTotal()));
        this.setPageNo(Convert.toInt(page.getCurrent()));
        this.setPageNum(Convert.toInt(page.getCurrent()));
        this.setPageSize(Convert.toInt(page.getSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(page.getTotal()), Convert.toInt(page.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()), Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }

    public PageResult(IPage<T> page, List<T> t) {
        this.setRows(t);
        this.setTotalRows(Convert.toInt(page.getTotal()));
        this.setPageNo(Convert.toInt(page.getCurrent()));
        this.setPageNum(Convert.toInt(page.getCurrent()));
        this.setPageSize(Convert.toInt(page.getSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(page.getTotal()), Convert.toInt(page.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()), Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }
}
