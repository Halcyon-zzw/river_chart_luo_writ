package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.SubCategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 小分类表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/sub-category")
@Tag(name = "小分类表管理", description = "小分类表相关接口")
@Validated
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    /**
     * 根据ID获取小分类表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取小分类表", description = "根据ID获取小分类表详情")
    public Result<SubCategoryDTO> getSubCategory(@PathVariable Long id) {
        SubCategoryDTO subCategoryDTO = subCategoryService.getById(id);
        if (subCategoryDTO != null) {
            return Result.success(subCategoryDTO);
        } else {
            throw new DataNotFoundException("小分类表", id);
        }
    }

    /**
     * 创建小分类表
     */
    @PostMapping("/create")
    @Operation(summary = "创建小分类表", description = "创建新的小分类表")
    public Result<SubCategoryDTO> createSubCategory(@RequestBody @Validated SubCategoryDTO subCategoryDTO) {
        SubCategoryDTO result = subCategoryService.save(subCategoryDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除小分类表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除小分类表", description = "根据ID删除小分类表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = subCategoryService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("小分类表", id);
        }
    }

    /**
     * 根据ID更新小分类表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新小分类表", description = "根据ID更新小分类表信息")
    public Result<SubCategoryDTO> updateById(@PathVariable Long id, @RequestBody @Validated SubCategoryDTO subCategoryDTO) {
        SubCategoryDTO result = subCategoryService.updateById(id, subCategoryDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("小分类表", id);
        }
    }

}
