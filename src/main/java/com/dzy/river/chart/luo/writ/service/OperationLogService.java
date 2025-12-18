package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.OperationLogDTO;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface OperationLogService {

    /**
     * 根据ID获取操作日志表
     *
     * @param id 主键ID
     * @return 操作日志表DTO对象
     */
    OperationLogDTO getById(Long id);

    /**
     * 保存操作日志表
     *
     * @param operationLogDTO 操作日志表DTO对象
     * @return 操作日志表DTO对象
     */
    OperationLogDTO save(OperationLogDTO operationLogDTO);

    /**
     * 根据ID删除操作日志表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新操作日志表
     *
     * @param id 主键ID
     * @param operationLogDTO 操作日志表DTO对象
     * @return 操作日志表DTO对象
     */
    OperationLogDTO updateById(Long id, OperationLogDTO operationLogDTO);

}
