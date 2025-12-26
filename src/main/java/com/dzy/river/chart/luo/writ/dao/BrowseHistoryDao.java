package com.dzy.river.chart.luo.writ.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.river.chart.luo.writ.domain.entity.BrowseHistory;
import com.dzy.river.chart.luo.writ.mapper.BrowseHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 浏览历史表 Dao类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
@Repository
public class BrowseHistoryDao extends ServiceImpl<BrowseHistoryMapper, BrowseHistory> {

    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;
}
