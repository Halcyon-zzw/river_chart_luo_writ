package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategoryTag;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryTagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 小分类标签关联表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface SubCategoryTagConvert {

    SubCategoryTagConvert INSTANCE = Mappers.getMapper(SubCategoryTagConvert.class);

    /**
     * SubCategoryTag 转 SubCategoryTagDTO
     *
     * @param subCategoryTag 小分类标签关联表实体
     * @return 小分类标签关联表DTO
     */
    SubCategoryTagDTO toSubCategoryTagDTO(SubCategoryTag subCategoryTag);

    /**
     * SubCategoryTagDTO 转 SubCategoryTag
     *
     * @param subCategoryTagDTO 小分类标签关联表DTO
     * @return 小分类标签关联表实体
     */
    SubCategoryTag toSubCategoryTag(SubCategoryTagDTO subCategoryTagDTO);

    /**
     * SubCategoryTag 列表转 SubCategoryTagDTO 列表
     *
     * @param subCategoryTagList 小分类标签关联表实体列表
     * @return 小分类标签关联表DTO列表
     */
    List<SubCategoryTagDTO> toSubCategoryTagDTOList(List<SubCategoryTag> subCategoryTagList);

    /**
     * SubCategoryTagDTO 列表转 SubCategoryTag 列表
     *
     * @param subCategoryTagDTOList 小分类标签关联表DTO列表
     * @return 小分类标签关联表实体列表
     */
    List<SubCategoryTag> toSubCategoryTagList(List<SubCategoryTagDTO> subCategoryTagDTOList);

}