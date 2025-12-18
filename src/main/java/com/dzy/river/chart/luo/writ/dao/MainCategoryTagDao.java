package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.MainCategoryTag;
import com.dzy.river.chart.luo.writ.mapper.MainCategoryTagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 主分类标签关联表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class MainCategoryTagDao extends ServiceImpl<MainCategoryTagMapper, MainCategoryTag> {

    @Autowired
    private MainCategoryTagMapper mainCategoryTagMapper;

}