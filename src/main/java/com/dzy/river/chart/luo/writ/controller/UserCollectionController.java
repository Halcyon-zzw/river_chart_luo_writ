package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.UserCollectionDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.req.CollectionPageReq;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.UserCollectionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 用户收藏表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/user-collection")
@Tag(name = "用户收藏表管理", description = "用户收藏表相关接口")
@Validated
public class UserCollectionController {

    @Autowired
    private UserCollectionService userCollectionService;

    /**
     * 根据ID获取用户收藏表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户收藏表", description = "根据ID获取用户收藏表详情")
    public Result<UserCollectionDTO> getUserCollection(@PathVariable Long id) {
        UserCollectionDTO userCollectionDTO = userCollectionService.getById(id);
        if (userCollectionDTO != null) {
            return Result.success(userCollectionDTO);
        } else {
            throw new DataNotFoundException("用户收藏表", id);
        }
    }

    /**
     * 创建用户收藏表
     */
    @PostMapping("/create")
    @Operation(summary = "创建用户收藏表", description = "创建新的用户收藏表")
    public Result<UserCollectionDTO> createUserCollection(@RequestBody @Validated UserCollectionDTO userCollectionDTO) {
        UserCollectionDTO result = userCollectionService.save(userCollectionDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除用户收藏表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户收藏表", description = "根据ID删除用户收藏表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = userCollectionService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("用户收藏表", id);
        }
    }

    /**
     * 根据ID更新用户收藏表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户收藏表", description = "根据ID更新用户收藏表信息")
    public Result<UserCollectionDTO> updateById(@PathVariable Long id, @RequestBody @Validated UserCollectionDTO userCollectionDTO) {
        UserCollectionDTO result = userCollectionService.updateById(id, userCollectionDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("用户收藏表", id);
        }
    }

    /**
     * 分页查询用户收藏列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询用户收藏", description = "根据用户ID和内容类型分页查询收藏列表")
    public Result<PageResult<UserCollectionDTO>> page(@RequestBody @Validated CollectionPageReq collectionPageReq) {
        PageResult<UserCollectionDTO> pageResult = userCollectionService.page(collectionPageReq);
        return Result.success(pageResult);
    }

}
