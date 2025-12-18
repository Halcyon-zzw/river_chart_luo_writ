package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.OperationLog;
import com.dzy.river.chart.luo.writ.mapper.OperationLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 操作日志表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class OperationLogDao extends ServiceImpl<OperationLogMapper, OperationLog> {

    @Autowired
    private OperationLogMapper operationLogMapper;

}