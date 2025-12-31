package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.User;
import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import com.dzy.river.chart.luo.writ.domain.convert.UserConvert;
import com.dzy.river.chart.luo.writ.dao.UserDao;
import com.dzy.river.chart.luo.writ.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConvert userConvert;

    @Override
    public UserDTO getById(Long id) {
        User user = userDao.getById(id);
        return user != null ? userConvert.toUserDTO(user) : null;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = userConvert.toUser(userDTO);
        boolean success = userDao.save(user);
        return success ? userConvert.toUserDTO(user) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return userDao.removeById(id);
    }

    @Override
    public UserDTO updateById(Long id, UserDTO userDTO) {
        userDTO.setId(id);
        User user = userConvert.toUser(userDTO);
        boolean success = userDao.updateById(user);
        return success ? userConvert.toUserDTO(user) : null;
    }

    @Override
    public UserDTO login(Object obj) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);

        return userDTO;
    }

}
