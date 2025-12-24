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

    /**
     * 批量关联标签到子分类
     * 先删除该子分类的所有旧关联，再批量插入新的关联记录
     *
     * @param subCategoryId 子分类ID
     * @param tagIds 标签ID列表
     * @return 成功关联的数量
     */
    int batchLinkTags(Long subCategoryId, java.util.List<Long> tagIds);

}
