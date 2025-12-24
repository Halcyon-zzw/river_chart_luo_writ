package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.req.BatchLinkMainCategoryTagReq;
import com.dzy.river.chart.luo.writ.domain.req.BatchLinkSubCategoryTagReq;
import com.dzy.river.chart.luo.writ.domain.req.BatchLinkContentTagReq;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.TagService;
import com.dzy.river.chart.luo.writ.service.MainCategoryTagService;
import com.dzy.river.chart.luo.writ.service.SubCategoryTagService;
import com.dzy.river.chart.luo.writ.service.ContentTagService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

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

    @Autowired
    private MainCategoryTagService mainCategoryTagService;

    @Autowired
    private SubCategoryTagService subCategoryTagService;

    @Autowired
    private ContentTagService contentTagService;

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

    /**
     * 查询标签列表
     */
    @GetMapping("/query")
    @Operation(summary = "查询标签", description = "根据名称模糊查询标签列表")
    public Result<List<TagDTO>> query(@RequestParam(required = false) String name) {
        List<TagDTO> tags = tagService.queryByName(name);
        return Result.success(tags);
    }

    /**
     * 主分类批量关联标签
     */
    @PostMapping("/batch-link-main-category")
    @Operation(summary = "主分类批量关联标签", description = "为主分类批量关联标签，会先删除旧关联再添加新关联")
    public Result<Integer> batchLinkMainCategoryTags(@RequestBody @Validated BatchLinkMainCategoryTagReq req) {
        int count = mainCategoryTagService.batchLinkTags(req.getMainCategoryId(), req.getTagIds());
        return Result.success("批量关联成功", count);
    }

    /**
     * 子分类批量关联标签
     */
    @PostMapping("/batch-link-sub-category")
    @Operation(summary = "子分类批量关联标签", description = "为子分类批量关联标签，会先删除旧关联再添加新关联")
    public Result<Integer> batchLinkSubCategoryTags(@RequestBody @Validated BatchLinkSubCategoryTagReq req) {
        int count = subCategoryTagService.batchLinkTags(req.getSubCategoryId(), req.getTagIds());
        return Result.success("批量关联成功", count);
    }

    /**
     * 内容批量关联标签
     */
    @PostMapping("/batch-link-content")
    @Operation(summary = "内容批量关联标签", description = "为内容批量关联标签，会先删除旧关联再添加新关联")
    public Result<Integer> batchLinkContentTags(@RequestBody @Validated BatchLinkContentTagReq req) {
        int count = contentTagService.batchLinkTags(req.getContentId(), req.getTagIds());
        return Result.success("批量关联成功", count);
    }

}
