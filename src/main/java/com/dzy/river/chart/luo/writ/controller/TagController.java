package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.TagService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/tag")
@Tag(name = "标签表管理", description = "标签表相关接口")
@Validated
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 根据ID获取标签表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取标签表", description = "根据ID获取标签表详情")
    public Result<TagDTO> getTag(@PathVariable Long id) {
        TagDTO tagDTO = tagService.getById(id);
        if (tagDTO != null) {
            return Result.success(tagDTO);
        } else {
            throw new DataNotFoundException("标签表", id);
        }
    }

    /**
     * 创建标签表
     */
    @PostMapping("/create")
    @Operation(summary = "创建标签表", description = "创建新的标签表")
    public Result<TagDTO> createTag(@RequestBody @Validated TagDTO tagDTO) {
        TagDTO result = tagService.save(tagDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除标签表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签表", description = "根据ID删除标签表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = tagService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("标签表", id);
        }
    }

    /**
     * 根据ID更新标签表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新标签表", description = "根据ID更新标签表信息")
    public Result<TagDTO> updateById(@PathVariable Long id, @RequestBody @Validated TagDTO tagDTO) {
        TagDTO result = tagService.updateById(id, tagDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("标签表", id);
        }
    }

}
