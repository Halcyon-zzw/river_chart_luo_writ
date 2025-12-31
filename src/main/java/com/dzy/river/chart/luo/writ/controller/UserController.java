package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户表管理", description = "用户表相关接口")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID获取用户表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户表", description = "根据ID获取用户表详情")
    public Result<UserDTO> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getById(id);
        if (userDTO != null) {
            return Result.success(userDTO);
        } else {
            throw new DataNotFoundException("用户表", id);
        }
    }

    /**
     * 创建用户表
     */
    @PostMapping("/create")
    @Operation(summary = "创建用户表", description = "创建新的用户表")
    public Result<UserDTO> createUser(@RequestBody @Validated UserDTO userDTO) {
        UserDTO result = userService.save(userDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除用户表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户表", description = "根据ID删除用户表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("用户表", id);
        }
    }

    /**
     * 根据ID更新用户表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户表", description = "根据ID更新用户表信息")
    public Result<UserDTO> updateById(@PathVariable Long id, @RequestBody @Validated UserDTO userDTO) {
        UserDTO result = userService.updateById(id, userDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("用户表", id);
        }
    }

    /**
     * 根据ID更新用户表
     */
    @PostMapping("/login")
    @Operation(summary = "登录", description = "根据ID更新用户表信息")
    public Result<Long> login(@RequestBody @Validated Object obj) {
        return Result.success(1L);
    }

}
