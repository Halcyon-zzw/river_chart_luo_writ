package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.req.ContentPageReq;
import com.dzy.river.chart.luo.writ.domain.req.CreateContentWithCategoriesReq;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.ContentService;
import com.dzy.river.chart.luo.writ.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * <p>
 * 数据内容表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/content")
@Tag(name = "数据内容表管理", description = "数据内容表相关接口")
@Validated
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 根据ID获取数据内容表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取数据内容表", description = "根据ID获取数据内容表详情")
    public Result<ContentDTO> getContent(@PathVariable Long id) {
        ContentDTO contentDTO = contentService.getById(id);
        if (contentDTO != null) {
            return Result.success(contentDTO);
        } else {
            throw new DataNotFoundException("数据内容表", id);
        }
    }

    /**
     * 创建数据内容表
     */
    @PostMapping("/create")
    @Operation(summary = "创建数据内容表", description = "创建新的数据内容表")
    public Result<ContentDTO> createContent(@RequestBody @Validated ContentDTO contentDTO) {
        ContentDTO result = contentService.save(contentDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除数据内容表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据内容表", description = "根据ID删除数据内容表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = contentService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("数据内容表", id);
        }
    }

    /**
     * 根据ID更新数据内容表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新数据内容表", description = "根据ID更新数据内容表信息")
    public Result<ContentDTO> updateById(@PathVariable Long id, @RequestBody @Validated ContentDTO contentDTO) {
        ContentDTO result = contentService.updateById(id, contentDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("数据内容表", id);
        }
    }

    /**
     * 分页查询内容列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询内容", description = "分页查询内容列表，包含关联的标签信息")
    public Result<PageResult<ContentDTO>> page(@RequestBody @Validated ContentPageReq contentPageReq) {
        PageResult<ContentDTO> pageResult = contentService.page(contentPageReq);
        return Result.success(pageResult);
    }

    /**
     * 关联标签
     */
    @PostMapping("/{id}/tags")
    @Operation(summary = "关联标签", description = "为内容关联标签")
    public Result<Boolean> associateTags(@PathVariable Long id, @RequestBody List<Long> tagIds) {
        boolean success = contentService.associateTags(id, tagIds);
        return Result.success("关联成功", success);
    }

    /**
     * 批量上传图片
     */
    @PostMapping("/upload-images")
    @Operation(summary = "批量上传图片", description = "批量上传图片文件，单次最多20个，返回图片访问URL列表")
    public Result<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        List<String> imageUrls = fileUploadService.uploadImages(files, 20);
        return Result.success("上传成功", imageUrls);
    }

    /**
     * 级联创建内容
     * 依次创建主分类、子分类和内容，并关联各自的标签
     */
    @PostMapping("/create-with-categories")
    @Operation(summary = "级联创建内容", description = "一次性创建主分类、子分类和内容，并关联各自的标签。保证事务一致性。")
    public Result<ContentDTO> createWithCategories(@RequestBody @Validated CreateContentWithCategoriesReq request) {
        ContentDTO result = contentService.createWithCategories(
                request.getMainCategory(),
                request.getSubCategory(),
                request.getContent()
        );
        return Result.success("创建成功", result);
    }

}
