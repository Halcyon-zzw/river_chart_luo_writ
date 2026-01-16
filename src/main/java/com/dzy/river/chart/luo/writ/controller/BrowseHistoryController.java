package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.dto.TimeRangeTypeDTO;
import com.dzy.river.chart.luo.writ.domain.req.BrowseHistoryPageReq;
import com.dzy.river.chart.luo.writ.domain.req.ClearReq;
import com.dzy.river.chart.luo.writ.domain.req.RecordBrowseReq;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.BrowseHistoryService;
import com.dzy.river.chart.luo.writ.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/create")
    @Operation(summary = "记录浏览", description = "记录用户浏览内容，如果已存在则更新浏览次数和时间。支持匿名浏览（未登录用户）")
    public Result<Boolean> recordBrowse(@RequestBody @Validated RecordBrowseReq req) {
        // 从当前登录用户获取 userId，如果未登录则为 null（允许匿名浏览）
        Long userId = UserUtil.getUserId();
        browseHistoryService.recordBrowse(req.getContentId(), userId);
        return Result.success("记录成功", true);
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
        Long userId = UserUtil.getUserId();
        pageReq.setUserId(userId);
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

    /**
     * 清空当前用户的浏览历史
     */
    @DeleteMapping("/clear")
    @Operation(summary = "清空浏览历史", description = "清空当前用户的所有浏览历史记录，可选按类型过滤")
    public Result<Integer> clearBrowseHistory(@RequestBody @Validated ClearReq clearReq) {
        // 从 ThreadLocal 中获取当前用户ID
        Long userId = UserUtil.getUserId();

        // 清空该用户的所有浏览历史（可按类型过滤）
        Integer count = browseHistoryService.clearByUserId(clearReq);

        return Result.success("已清空 " + count + " 条浏览历史", count);
    }


    @GetMapping("/listTimeRangeTypeList")
    @Operation(summary = "获取浏览历史时间类型列表")
    public Result<List<TimeRangeTypeDTO>> listTimeRangeTypeList() {
        List<TimeRangeTypeDTO> pageResult = browseHistoryService.listTimeRangeTypeList();
        return Result.success(pageResult);
    }
}
