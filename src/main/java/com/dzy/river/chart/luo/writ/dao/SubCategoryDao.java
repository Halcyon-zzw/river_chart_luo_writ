package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategory;
import com.dzy.river.chart.luo.writ.mapper.SubCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 小分类表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class SubCategoryDao extends ServiceImpl<SubCategoryMapper, SubCategory> {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

}