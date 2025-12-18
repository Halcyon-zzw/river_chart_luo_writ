package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryTagDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.SubCategoryTagService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 小分类标签关联表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/sub-category-tag")
@Tag(name = "小分类标签关联表管理", description = "小分类标签关联表相关接口")
@Validated
public class SubCategoryTagController {

    @Autowired
    private SubCategoryTagService subCategoryTagService;

    /**
     * 根据ID获取小分类标签关联表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取小分类标签关联表", description = "根据ID获取小分类标签关联表详情")
    public Result<SubCategoryTagDTO> getSubCategoryTag(@PathVariable Long id) {
        SubCategoryTagDTO subCategoryTagDTO = subCategoryTagService.getById(id);
        if (subCategoryTagDTO != null) {
            return Result.success(subCategoryTagDTO);
        } else {
            throw new DataNotFoundException("小分类标签关联表", id);
        }
    }

    /**
     * 创建小分类标签关联表
     */
    @PostMapping("/create")
    @Operation(summary = "创建小分类标签关联表", description = "创建新的小分类标签关联表")
    public Result<SubCategoryTagDTO> createSubCategoryTag(@RequestBody @Validated SubCategoryTagDTO subCategoryTagDTO) {
        SubCategoryTagDTO result = subCategoryTagService.save(subCategoryTagDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除小分类标签关联表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除小分类标签关联表", description = "根据ID删除小分类标签关联表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = subCategoryTagService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("小分类标签关联表", id);
        }
    }

    /**
     * 根据ID更新小分类标签关联表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新小分类标签关联表", description = "根据ID更新小分类标签关联表信息")
    public Result<SubCategoryTagDTO> updateById(@PathVariable Long id, @RequestBody @Validated SubCategoryTagDTO subCategoryTagDTO) {
        SubCategoryTagDTO result = subCategoryTagService.updateById(id, subCategoryTagDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("小分类标签关联表", id);
        }
    }

}
