package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.req.ContentPageReq;

import java.util.List;

/**
 * <p>
 * 数据内容表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface ContentService {

    /**
     * 根据ID获取数据内容表
     *
     * @param id 主键ID
     * @return 数据内容表DTO对象
     */
    ContentDTO getById(Long id);

    /**
     * 保存数据内容表
     *
     * @param contentDTO 数据内容表DTO对象
     * @return 数据内容表DTO对象
     */
    ContentDTO save(ContentDTO contentDTO);

    /**
     * 根据ID删除数据内容表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新数据内容表
     *
     * @param id 主键ID
     * @param contentDTO 数据内容表DTO对象
     * @return 数据内容表DTO对象
     */
    ContentDTO updateById(Long id, ContentDTO contentDTO);

    /**
     * 分页查询内容列表
     *
     * @param contentPageReq 分页请求参数
     * @return 分页结果
     */
    PageResult<ContentDTO> page(ContentPageReq contentPageReq);

    /**
     * 关联标签
     *
     * @param contentId 内容ID
     * @param tagIds 标签ID列表
     * @return 是否关联成功
     */
    boolean associateTags(Long contentId, List<Long> tagIds);

    /**
     * 级联创建内容
     * 依次创建主分类、子分类和内容，并关联各自的标签
     *
     * @param mainCategoryDTO 主分类DTO
     * @param subCategoryDTO 子分类DTO
     * @param contentDTO 内容DTO
     * @return 创建成功的内容DTO（包含标签列表）
     */
    ContentDTO createWithCategories(MainCategoryDTO mainCategoryDTO, SubCategoryDTO subCategoryDTO, ContentDTO contentDTO);

}
