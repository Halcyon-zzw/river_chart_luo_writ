package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.mapper.TagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 标签表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class TagDao extends ServiceImpl<TagMapper, Tag> {

    @Autowired
    private TagMapper tagMapper;

}