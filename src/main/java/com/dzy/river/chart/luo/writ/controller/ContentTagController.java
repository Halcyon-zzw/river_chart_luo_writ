package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.ContentTagDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.ContentTagService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 内容标签关联表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/content-tag")
@Tag(name = "内容标签关联表管理", description = "内容标签关联表相关接口")
@Validated
public class ContentTagController {

    @Autowired
    private ContentTagService contentTagService;

    /**
     * 根据ID获取内容标签关联表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取内容标签关联表", description = "根据ID获取内容标签关联表详情")
    public Result<ContentTagDTO> getContentTag(@PathVariable Long id) {
        ContentTagDTO contentTagDTO = contentTagService.getById(id);
        if (contentTagDTO != null) {
            return Result.success(contentTagDTO);
        } else {
            throw new DataNotFoundException("内容标签关联表", id);
        }
    }

    /**
     * 创建内容标签关联表
     */
    @PostMapping
    @Operation(summary = "创建内容标签关联表", description = "创建新的内容标签关联表")
    public Result<ContentTagDTO> createContentTag(@RequestBody @Validated ContentTagDTO contentTagDTO) {
        ContentTagDTO result = contentTagService.save(contentTagDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除内容标签关联表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除内容标签关联表", description = "根据ID删除内容标签关联表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = contentTagService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("内容标签关联表", id);
        }
    }

    /**
     * 根据ID更新内容标签关联表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新内容标签关联表", description = "根据ID更新内容标签关联表信息")
    public Result<ContentTagDTO> updateById(@PathVariable Long id, @RequestBody @Validated ContentTagDTO contentTagDTO) {
        ContentTagDTO result = contentTagService.updateById(id, contentTagDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("内容标签关联表", id);
        }
    }

}
