package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryTagDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.MainCategoryTagService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 主分类标签关联表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/main-category-tag")
@Tag(name = "主分类标签关联表管理", description = "主分类标签关联表相关接口")
@Validated
public class MainCategoryTagController {

    @Autowired
    private MainCategoryTagService mainCategoryTagService;

    /**
     * 根据ID获取主分类标签关联表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取主分类标签关联表", description = "根据ID获取主分类标签关联表详情")
    public Result<MainCategoryTagDTO> getMainCategoryTag(@PathVariable Long id) {
        MainCategoryTagDTO mainCategoryTagDTO = mainCategoryTagService.getById(id);
        if (mainCategoryTagDTO != null) {
            return Result.success(mainCategoryTagDTO);
        } else {
            throw new DataNotFoundException("主分类标签关联表", id);
        }
    }

    /**
     * 创建主分类标签关联表
     */
    @PostMapping
    @Operation(summary = "创建主分类标签关联表", description = "创建新的主分类标签关联表")
    public Result<MainCategoryTagDTO> createMainCategoryTag(@RequestBody @Validated MainCategoryTagDTO mainCategoryTagDTO) {
        MainCategoryTagDTO result = mainCategoryTagService.save(mainCategoryTagDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除主分类标签关联表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除主分类标签关联表", description = "根据ID删除主分类标签关联表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = mainCategoryTagService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("主分类标签关联表", id);
        }
    }

    /**
     * 根据ID更新主分类标签关联表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新主分类标签关联表", description = "根据ID更新主分类标签关联表信息")
    public Result<MainCategoryTagDTO> updateById(@PathVariable Long id, @RequestBody @Validated MainCategoryTagDTO mainCategoryTagDTO) {
        MainCategoryTagDTO result = mainCategoryTagService.updateById(id, mainCategoryTagDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("主分类标签关联表", id);
        }
    }

}
