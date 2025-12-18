package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface TagService {

    /**
     * 根据ID获取标签表
     *
     * @param id 主键ID
     * @return 标签表DTO对象
     */
    TagDTO getById(Long id);

    /**
     * 保存标签表
     *
     * @param tagDTO 标签表DTO对象
     * @return 标签表DTO对象
     */
    TagDTO save(TagDTO tagDTO);

    /**
     * 根据ID删除标签表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 根据ID更新标签表
     *
     * @param id 主键ID
     * @param tagDTO 标签表DTO对象
     * @return 标签表DTO对象
     */
    TagDTO updateById(Long id, TagDTO tagDTO);

}
