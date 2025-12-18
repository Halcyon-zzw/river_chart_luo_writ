package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.OperationLogDTO;
import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.exception.DataNotFoundException;
import com.dzy.river.chart.luo.writ.service.OperationLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@RestController
@RequestMapping("/operation-log")
@Tag(name = "操作日志表管理", description = "操作日志表相关接口")
@Validated
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 根据ID获取操作日志表
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取操作日志表", description = "根据ID获取操作日志表详情")
    public Result<OperationLogDTO> getOperationLog(@PathVariable Long id) {
        OperationLogDTO operationLogDTO = operationLogService.getById(id);
        if (operationLogDTO != null) {
            return Result.success(operationLogDTO);
        } else {
            throw new DataNotFoundException("操作日志表", id);
        }
    }

    /**
     * 创建操作日志表
     */
    @PostMapping("/create")
    @Operation(summary = "创建操作日志表", description = "创建新的操作日志表")
    public Result<OperationLogDTO> createOperationLog(@RequestBody @Validated OperationLogDTO operationLogDTO) {
        OperationLogDTO result = operationLogService.save(operationLogDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID删除操作日志表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志表", description = "根据ID删除操作日志表")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = operationLogService.removeById(id);
        if (success) {
            return Result.success("删除成功", true);
        } else {
            throw new DataNotFoundException("操作日志表", id);
        }
    }

    /**
     * 根据ID更新操作日志表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新操作日志表", description = "根据ID更新操作日志表信息")
    public Result<OperationLogDTO> updateById(@PathVariable Long id, @RequestBody @Validated OperationLogDTO operationLogDTO) {
        OperationLogDTO result = operationLogService.updateById(id, operationLogDTO);
        if (result != null) {
            return Result.success("更新成功", result);
        } else {
            throw new DataNotFoundException("操作日志表", id);
        }
    }

}
