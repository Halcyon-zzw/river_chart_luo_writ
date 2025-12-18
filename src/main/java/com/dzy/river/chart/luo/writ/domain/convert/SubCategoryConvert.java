package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategory;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 小分类表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface SubCategoryConvert {

    SubCategoryConvert INSTANCE = Mappers.getMapper(SubCategoryConvert.class);

    /**
     * SubCategory 转 SubCategoryDTO
     *
     * @param subCategory 小分类表实体
     * @return 小分类表DTO
     */
    SubCategoryDTO toSubCategoryDTO(SubCategory subCategory);

    /**
     * SubCategoryDTO 转 SubCategory
     *
     * @param subCategoryDTO 小分类表DTO
     * @return 小分类表实体
     */
    SubCategory toSubCategory(SubCategoryDTO subCategoryDTO);

    /**
     * SubCategory 列表转 SubCategoryDTO 列表
     *
     * @param subCategoryList 小分类表实体列表
     * @return 小分类表DTO列表
     */
    List<SubCategoryDTO> toSubCategoryDTOList(List<SubCategory> subCategoryList);

    /**
     * SubCategoryDTO 列表转 SubCategory 列表
     *
     * @param subCategoryDTOList 小分类表DTO列表
     * @return 小分类表实体列表
     */
    List<SubCategory> toSubCategoryList(List<SubCategoryDTO> subCategoryDTOList);

}