package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.req.MainCategoryPageReq;

/**
 * <p>
 * 主分类表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface MainCategoryService {

    /**
     * 根据ID获取主分类表
     *
     * @param id 主键ID
     * @return 主分类表DTO对象
     */
    MainCategoryDTO getById(Long id);

    /**
     * 保存主分类表
     *
     * @param mainCategoryDTO 主分类表DTO对象
     * @return 主分类表DTO对象
     */
    MainCategoryDTO save(MainCategoryDTO mainCategoryDTO);

    /**
     * 根据ID删除主分类表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新主分类表
     *
     * @param id 主键ID
     * @param mainCategoryDTO 主分类表DTO对象
     * @return 主分类表DTO对象
     */
    MainCategoryDTO updateById(Long id, MainCategoryDTO mainCategoryDTO);

    /**
     * 分页查询主分类列表
     *
     * @param mainCategoryPageReq 分页请求参数
     * @return 分页结果
     */
    PageResult<MainCategoryDTO> page(MainCategoryPageReq mainCategoryPageReq);

}
