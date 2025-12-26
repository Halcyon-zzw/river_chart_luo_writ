package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.req.BrowseHistoryPageReq;
import com.dzy.river.chart.luo.writ.domain.req.RecordBrowseReq;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.BrowseHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 浏览历史表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
@RestController
@RequestMapping("/browse-history")
@Tag(name = "浏览历史管理", description = "浏览历史相关接口")
@Validated
public class BrowseHistoryController {

    @Autowired
    private BrowseHistoryService browseHistoryService;

    /**
     * 记录浏览
     */
    @PostMapping("/record")
    @Operation(summary = "记录浏览", description = "记录用户浏览内容，如果已存在则更新浏览次数和时间")
    public Result<BrowseHistoryDTO> recordBrowse(@RequestBody @Validated RecordBrowseReq req) {
        BrowseHistoryDTO result = browseHistoryService.recordBrowse(req.getContentId(), req.getUserId());
        return Result.success("记录成功", result);
    }

    /**
     * 根据ID获取浏览历史
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取浏览历史", description = "根据ID获取浏览历史详情")
    public Result<BrowseHistoryDTO> getBrowseHistory(@PathVariable Long id) {
        BrowseHistoryDTO browseHistoryDTO = browseHistoryService.getById(id);
        if (browseHistoryDTO != null) {
            return Result.success(browseHistoryDTO);
        } else {
            throw new DataNotFoundException("浏览历史", id);
        }
    }

    /**
     * 根据ID删除浏览历史
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除浏览历史", description = "根据ID删除浏览历史")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = browseHistoryService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("浏览历史", id);
        }
    }

    /**
     * 分页查询浏览历史
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询浏览历史", description = "分页查询浏览历史列表，支持按用户ID或内容ID过滤")
    public Result<PageResult<BrowseHistoryDTO>> page(@RequestBody @Validated BrowseHistoryPageReq pageReq) {
        PageResult<BrowseHistoryDTO> pageResult = browseHistoryService.page(pageReq);
        return Result.success(pageResult);
    }

    /**
     * 获取内容的浏览次数
     */
    @GetMapping("/content/{contentId}/count")
    @Operation(summary = "获取内容浏览次数", description = "获取某个内容的总浏览次数")
    public Result<Integer> getContentBrowseCount(@PathVariable Long contentId) {
        Integer count = browseHistoryService.getContentBrowseCount(contentId);
        return Result.success(count);
    }
}
