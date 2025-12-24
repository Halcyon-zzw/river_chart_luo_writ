package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryTagDTO;

/**
 * <p>
 * 主分类标签关联表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface MainCategoryTagService {

    /**
     * 根据ID获取主分类标签关联表
     *
     * @param id 主键ID
     * @return 主分类标签关联表DTO对象
     */
    MainCategoryTagDTO getById(Long id);

    /**
     * 保存主分类标签关联表
     *
     * @param mainCategoryTagDTO 主分类标签关联表DTO对象
     * @return 主分类标签关联表DTO对象
     */
    MainCategoryTagDTO save(MainCategoryTagDTO mainCategoryTagDTO);

    /**
     * 根据ID删除主分类标签关联表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新主分类标签关联表
     *
     * @param id 主键ID
     * @param mainCategoryTagDTO 主分类标签关联表DTO对象
     * @return 主分类标签关联表DTO对象
     */
    MainCategoryTagDTO updateById(Long id, MainCategoryTagDTO mainCategoryTagDTO);

    /**
     * 批量关联标签到主分类
     * 先删除该主分类的所有旧关联，再批量插入新的关联记录
     *
     * @param mainCategoryId 主分类ID
     * @param tagIds 标签ID列表
     * @return 成功关联的数量
     */
    int batchLinkTags(Long mainCategoryId, java.util.List<Long> tagIds);

}
