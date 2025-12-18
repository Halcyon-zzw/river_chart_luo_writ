package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.ContentTagDTO;

/**
 * <p>
 * 内容标签关联表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface ContentTagService {

    /**
     * 根据ID获取内容标签关联表
     *
     * @param id 主键ID
     * @return 内容标签关联表DTO对象
     */
    ContentTagDTO getById(Long id);

    /**
     * 保存内容标签关联表
     *
     * @param contentTagDTO 内容标签关联表DTO对象
     * @return 内容标签关联表DTO对象
     */
    ContentTagDTO save(ContentTagDTO contentTagDTO);

    /**
     * 根据ID删除内容标签关联表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新内容标签关联表
     *
     * @param id 主键ID
     * @param contentTagDTO 内容标签关联表DTO对象
     * @return 内容标签关联表DTO对象
     */
    ContentTagDTO updateById(Long id, ContentTagDTO contentTagDTO);

}
