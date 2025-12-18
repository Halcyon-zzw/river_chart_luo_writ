package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryTagDTO;

/**
 * <p>
 * 小分类标签关联表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface SubCategoryTagService {

    /**
     * 根据ID获取小分类标签关联表
     *
     * @param id 主键ID
     * @return 小分类标签关联表DTO对象
     */
    SubCategoryTagDTO getById(Long id);

    /**
     * 保存小分类标签关联表
     *
     * @param subCategoryTagDTO 小分类标签关联表DTO对象
     * @return 小分类标签关联表DTO对象
     */
    SubCategoryTagDTO save(SubCategoryTagDTO subCategoryTagDTO);

    /**
     * 根据ID删除小分类标签关联表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新小分类标签关联表
     *
     * @param id 主键ID
     * @param subCategoryTagDTO 小分类标签关联表DTO对象
     * @return 小分类标签关联表DTO对象
     */
    SubCategoryTagDTO updateById(Long id, SubCategoryTagDTO subCategoryTagDTO);

}
