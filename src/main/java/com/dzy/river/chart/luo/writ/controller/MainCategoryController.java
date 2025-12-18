package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.req.MainCategoryPageReq;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.MainCategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * <p>
 * 主分类表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/main-category")
@Tag(name = "主分类表管理", description = "主分类表相关接口")
@Validated
public class MainCategoryController {

    @Autowired
    private MainCategoryService mainCategoryService;

    /**
     * 根据ID获取主分类表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取主分类表", description = "根据ID获取主分类表详情")
    public Result<MainCategoryDTO> getMainCategory(@PathVariable Long id) {
        MainCategoryDTO mainCategoryDTO = mainCategoryService.getById(id);
        if (mainCategoryDTO != null) {
            return Result.success(mainCategoryDTO);
        } else {
            throw new DataNotFoundException("主分类表", id);
        }
    }

    /**
     * 创建主分类表
     */
    @PostMapping("/create")
    @Operation(summary = "创建主分类表", description = "创建新的主分类表")
    public Result<MainCategoryDTO> createMainCategory(@RequestBody @Validated MainCategoryDTO mainCategoryDTO) {
        MainCategoryDTO result = mainCategoryService.save(mainCategoryDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除主分类表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除主分类表", description = "根据ID删除主分类表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = mainCategoryService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("主分类表", id);
        }
    }

    /**
     * 根据ID更新主分类表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新主分类表", description = "根据ID更新主分类表信息")
    public Result<MainCategoryDTO> updateById(@PathVariable Long id, @RequestBody @Validated MainCategoryDTO mainCategoryDTO) {
        MainCategoryDTO result = mainCategoryService.updateById(id, mainCategoryDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("主分类表", id);
        }
    }

    /**
     * 分页查询主分类列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询主分类", description = "分页查询主分类列表，包含关联的标签信息")
    public Result<PageResult<MainCategoryDTO>> page(@RequestBody @Validated MainCategoryPageReq mainCategoryPageReq) {
        PageResult<MainCategoryDTO> pageResult = mainCategoryService.page(mainCategoryPageReq);
        return Result.success(pageResult);
    }

    /**
     * 关联标签
     */
    @PostMapping("/{id}/tags")
    @Operation(summary = "关联标签", description = "为主分类关联标签")
    public Result<Boolean> associateTags(@PathVariable Long id, @RequestBody List<Long> tagIds) {
        boolean success = mainCategoryService.associateTags(id, tagIds);
        return Result.success("关联成功", success);
    }

}
