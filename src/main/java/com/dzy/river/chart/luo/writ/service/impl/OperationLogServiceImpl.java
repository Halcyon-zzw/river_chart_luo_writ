package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.OperationLog;
import com.dzy.river.chart.luo.writ.domain.dto.OperationLogDTO;
import com.dzy.river.chart.luo.writ.domain.convert.OperationLogConvert;
import com.dzy.river.chart.luo.writ.dao.OperationLogDao;
import com.dzy.river.chart.luo.writ.service.OperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogDao operationLogDao;

    @Autowired
    private OperationLogConvert operationLogConvert;

    @Override
    public OperationLogDTO getById(Long id) {
        OperationLog operationLog = operationLogDao.getById(id);
        return operationLog != null ? operationLogConvert.toOperationLogDTO(operationLog) : null;
    }

    @Override
    public OperationLogDTO save(OperationLogDTO operationLogDTO) {
        OperationLog operationLog = operationLogConvert.toOperationLog(operationLogDTO);
        boolean success = operationLogDao.save(operationLog);
        return success ? operationLogConvert.toOperationLogDTO(operationLog) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return operationLogDao.removeById(id);
    }

    @Override
    public OperationLogDTO updateById(Long id, OperationLogDTO operationLogDTO) {
        operationLogDTO.setId(id);
        OperationLog operationLog = operationLogConvert.toOperationLog(operationLogDTO);
        boolean success = operationLogDao.updateById(operationLog);
        return success ? operationLogConvert.toOperationLogDTO(operationLog) : null;
    }

}
