package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.MainCategory;
import com.dzy.river.chart.luo.writ.mapper.MainCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 主分类表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class MainCategoryDao extends ServiceImpl<MainCategoryMapper, MainCategory> {

    @Autowired
    private MainCategoryMapper mainCategoryMapper;

}