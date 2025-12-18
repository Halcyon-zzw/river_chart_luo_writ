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

}
