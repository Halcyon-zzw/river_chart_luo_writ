package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface UserService {

    /**
     * 根据ID获取用户表
     *
     * @param id 主键ID
     * @return 用户表DTO对象
     */
    UserDTO getById(Long id);

    /**
     * 保存用户表
     *
     * @param userDTO 用户表DTO对象
     * @return 用户表DTO对象
     */
    UserDTO save(UserDTO userDTO);

    /**
     * 根据ID删除用户表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新用户表
     *
     * @param id 主键ID
     * @param userDTO 用户表DTO对象
     * @return 用户表DTO对象
     */
    UserDTO updateById(Long id, UserDTO userDTO);

    UserDTO login(Object obj);
}
