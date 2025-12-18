package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.UserCollection;
import com.dzy.river.chart.luo.writ.domain.dto.UserCollectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 用户收藏表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface UserCollectionConvert {

    UserCollectionConvert INSTANCE = Mappers.getMapper(UserCollectionConvert.class);

    /**
     * UserCollection 转 UserCollectionDTO
     *
     * @param userCollection 用户收藏表实体
     * @return 用户收藏表DTO
     */
    UserCollectionDTO toUserCollectionDTO(UserCollection userCollection);

    /**
     * UserCollectionDTO 转 UserCollection
     *
     * @param userCollectionDTO 用户收藏表DTO
     * @return 用户收藏表实体
     */
    UserCollection toUserCollection(UserCollectionDTO userCollectionDTO);

    /**
     * UserCollection 列表转 UserCollectionDTO 列表
     *
     * @param userCollectionList 用户收藏表实体列表
     * @return 用户收藏表DTO列表
     */
    List<UserCollectionDTO> toUserCollectionDTOList(List<UserCollection> userCollectionList);

    /**
     * UserCollectionDTO 列表转 UserCollection 列表
     *
     * @param userCollectionDTOList 用户收藏表DTO列表
     * @return 用户收藏表实体列表
     */
    List<UserCollection> toUserCollectionList(List<UserCollectionDTO> userCollectionDTOList);

}