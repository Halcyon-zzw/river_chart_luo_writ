package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.req.SubCategoryPageReq;

import java.util.List;

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

    /**
     * 分页查询小分类列表
     *
     * @param subCategoryPageReq 分页请求参数
     * @return 分页结果
     */
    PageResult<SubCategoryDTO> page(SubCategoryPageReq subCategoryPageReq);

    /**
     * 关联标签
     *
     * @param subCategoryId 小分类ID
     * @param tagIds 标签ID列表
     * @return 是否关联成功
     */
    boolean associateTags(Long subCategoryId, List<Long> tagIds);

}
