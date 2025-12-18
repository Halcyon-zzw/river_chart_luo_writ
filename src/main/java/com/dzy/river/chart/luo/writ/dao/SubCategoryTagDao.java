package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategoryTag;
import com.dzy.river.chart.luo.writ.mapper.SubCategoryTagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 小分类标签关联表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class SubCategoryTagDao extends ServiceImpl<SubCategoryTagMapper, SubCategoryTag> {

    @Autowired
    private SubCategoryTagMapper subCategoryTagMapper;

}