package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.User;
import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 用户表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * User 转 UserDTO
     *
     * @param user 用户表实体
     * @return 用户表DTO
     */
    UserDTO toUserDTO(User user);

    /**
     * UserDTO 转 User
     *
     * @param userDTO 用户表DTO
     * @return 用户表实体
     */
    User toUser(UserDTO userDTO);

    /**
     * User 列表转 UserDTO 列表
     *
     * @param userList 用户表实体列表
     * @return 用户表DTO列表
     */
    List<UserDTO> toUserDTOList(List<User> userList);

    /**
     * UserDTO 列表转 User 列表
     *
     * @param userDTOList 用户表DTO列表
     * @return 用户表实体列表
     */
    List<User> toUserList(List<UserDTO> userDTOList);

}