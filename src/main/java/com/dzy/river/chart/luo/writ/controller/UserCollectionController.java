package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.UserCollectionDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.req.CollectionPageReq;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.UserCollectionService;
import com.dzy.river.chart.luo.writ.util.UserUtil;
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
    @Operation(summary = "创建用户收藏", description = "创建新的收藏记录，用户ID从登录用户获取")
    public Result<UserCollectionDTO> createUserCollection(@RequestBody @Validated UserCollectionDTO userCollectionDTO) {
        // 从当前登录用户获取 userId，防止用户伪造他人 userId
        Long userId = UserUtil.getUserId();
        userCollectionDTO.setUserId(userId);

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
    @Operation(summary = "分页查询用户收藏", description = "分页查询当前用户的收藏列表，按内容类型过滤")
    public Result<PageResult<UserCollectionDTO>> page(@RequestBody @Validated CollectionPageReq collectionPageReq) {
        // 从当前登录用户获取 userId，只能查看自己的收藏
        Long userId = UserUtil.getUserId();
        collectionPageReq.setUserId(userId);

        PageResult<UserCollectionDTO> pageResult = userCollectionService.page(collectionPageReq);
        return Result.success(pageResult);
    }

}
