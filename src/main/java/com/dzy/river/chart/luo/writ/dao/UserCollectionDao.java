package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.UserCollection;
import com.dzy.river.chart.luo.writ.mapper.UserCollectionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
    * 用户收藏表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class UserCollectionDao extends ServiceImpl<UserCollectionMapper, UserCollection> {

    @Autowired
    private UserCollectionMapper userCollectionMapper;

}