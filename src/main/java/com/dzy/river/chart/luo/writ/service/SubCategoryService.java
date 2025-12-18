package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;

/**
 * <p>
 * 小分类表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface SubCategoryService {

    /**
     * 根据ID获取小分类表
     *
     * @param id 主键ID
     * @return 小分类表DTO对象
     */
    SubCategoryDTO getById(Long id);

    /**
     * 保存小分类表
     *
     * @param subCategoryDTO 小分类表DTO对象
     * @return 小分类表DTO对象
     */
    SubCategoryDTO save(SubCategoryDTO subCategoryDTO);

    /**
     * 根据ID删除小分类表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新小分类表
     *
     * @param id 主键ID
     * @param subCategoryDTO 小分类表DTO对象
     * @return 小分类表DTO对象
     */
    SubCategoryDTO updateById(Long id, SubCategoryDTO subCategoryDTO);

}
