package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.MainCategory;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 主分类表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface MainCategoryConvert {

    MainCategoryConvert INSTANCE = Mappers.getMapper(MainCategoryConvert.class);

    /**
     * MainCategory 转 MainCategoryDTO
     *
     * @param mainCategory 主分类表实体
     * @return 主分类表DTO
     */
    MainCategoryDTO toMainCategoryDTO(MainCategory mainCategory);

    /**
     * MainCategoryDTO 转 MainCategory
     *
     * @param mainCategoryDTO 主分类表DTO
     * @return 主分类表实体
     */
    MainCategory toMainCategory(MainCategoryDTO mainCategoryDTO);

    /**
     * MainCategory 列表转 MainCategoryDTO 列表
     *
     * @param mainCategoryList 主分类表实体列表
     * @return 主分类表DTO列表
     */
    List<MainCategoryDTO> toMainCategoryDTOList(List<MainCategory> mainCategoryList);

    /**
     * MainCategoryDTO 列表转 MainCategory 列表
     *
     * @param mainCategoryDTOList 主分类表DTO列表
     * @return 主分类表实体列表
     */
    List<MainCategory> toMainCategoryList(List<MainCategoryDTO> mainCategoryDTOList);

}