package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.MainCategoryTag;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryTagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 主分类标签关联表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface MainCategoryTagConvert {

    MainCategoryTagConvert INSTANCE = Mappers.getMapper(MainCategoryTagConvert.class);

    /**
     * MainCategoryTag 转 MainCategoryTagDTO
     *
     * @param mainCategoryTag 主分类标签关联表实体
     * @return 主分类标签关联表DTO
     */
    MainCategoryTagDTO toMainCategoryTagDTO(MainCategoryTag mainCategoryTag);

    /**
     * MainCategoryTagDTO 转 MainCategoryTag
     *
     * @param mainCategoryTagDTO 主分类标签关联表DTO
     * @return 主分类标签关联表实体
     */
    MainCategoryTag toMainCategoryTag(MainCategoryTagDTO mainCategoryTagDTO);

    /**
     * MainCategoryTag 列表转 MainCategoryTagDTO 列表
     *
     * @param mainCategoryTagList 主分类标签关联表实体列表
     * @return 主分类标签关联表DTO列表
     */
    List<MainCategoryTagDTO> toMainCategoryTagDTOList(List<MainCategoryTag> mainCategoryTagList);

    /**
     * MainCategoryTagDTO 列表转 MainCategoryTag 列表
     *
     * @param mainCategoryTagDTOList 主分类标签关联表DTO列表
     * @return 主分类标签关联表实体列表
     */
    List<MainCategoryTag> toMainCategoryTagList(List<MainCategoryTagDTO> mainCategoryTagDTOList);

}