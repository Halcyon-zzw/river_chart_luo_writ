package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.OperationLog;
import com.dzy.river.chart.luo.writ.domain.dto.OperationLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 操作日志表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface OperationLogConvert {

    OperationLogConvert INSTANCE = Mappers.getMapper(OperationLogConvert.class);

    /**
     * OperationLog 转 OperationLogDTO
     *
     * @param operationLog 操作日志表实体
     * @return 操作日志表DTO
     */
    OperationLogDTO toOperationLogDTO(OperationLog operationLog);

    /**
     * OperationLogDTO 转 OperationLog
     *
     * @param operationLogDTO 操作日志表DTO
     * @return 操作日志表实体
     */
    OperationLog toOperationLog(OperationLogDTO operationLogDTO);

    /**
     * OperationLog 列表转 OperationLogDTO 列表
     *
     * @param operationLogList 操作日志表实体列表
     * @return 操作日志表DTO列表
     */
    List<OperationLogDTO> toOperationLogDTOList(List<OperationLog> operationLogList);

    /**
     * OperationLogDTO 列表转 OperationLog 列表
     *
     * @param operationLogDTOList 操作日志表DTO列表
     * @return 操作日志表实体列表
     */
    List<OperationLog> toOperationLogList(List<OperationLogDTO> operationLogDTOList);

}