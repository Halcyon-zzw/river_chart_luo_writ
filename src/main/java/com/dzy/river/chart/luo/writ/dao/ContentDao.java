package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.mapper.ContentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 数据内容表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class ContentDao extends ServiceImpl<ContentMapper, Content> {

    @Autowired
    private ContentMapper contentMapper;

}