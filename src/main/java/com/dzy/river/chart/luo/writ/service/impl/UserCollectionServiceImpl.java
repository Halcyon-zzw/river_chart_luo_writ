package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.UserCollection;
import com.dzy.river.chart.luo.writ.domain.dto.UserCollectionDTO;
import com.dzy.river.chart.luo.writ.domain.convert.UserCollectionConvert;
import com.dzy.river.chart.luo.writ.dao.UserCollectionDao;
import com.dzy.river.chart.luo.writ.service.UserCollectionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 用户收藏表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class UserCollectionServiceImpl implements UserCollectionService {

    @Autowired
    private UserCollectionDao userCollectionDao;

    @Autowired
    private UserCollectionConvert userCollectionConvert;

    @Override
    public UserCollectionDTO getById(Long id) {
        UserCollection userCollection = userCollectionDao.getById(id);
        return userCollection != null ? userCollectionConvert.toUserCollectionDTO(userCollection) : null;
    }

    @Override
    public UserCollectionDTO save(UserCollectionDTO userCollectionDTO) {
        UserCollection userCollection = userCollectionConvert.toUserCollection(userCollectionDTO);
        boolean success = userCollectionDao.save(userCollection);
        return success ? userCollectionConvert.toUserCollectionDTO(userCollection) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return userCollectionDao.removeById(id);
    }

    @Override
    public UserCollectionDTO updateById(Long id, UserCollectionDTO userCollectionDTO) {
        userCollectionDTO.setId(id);
        UserCollection userCollection = userCollectionConvert.toUserCollection(userCollectionDTO);
        boolean success = userCollectionDao.updateById(userCollection);
        return success ? userCollectionConvert.toUserCollectionDTO(userCollection) : null;
    }

}
