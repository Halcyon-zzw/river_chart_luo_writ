package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.req.BrowseHistoryPageReq;
import com.dzy.river.chart.luo.writ.domain.req.ClearReq;

/**
 * <p>
 * 浏览历史表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
public interface BrowseHistoryService {

    /**
     * 记录浏览
     * 如果记录已存在则更新浏览次数和最后浏览时间，否则插入新记录
     *
     * @param contentId 内容ID
     * @param userId 用户ID（可为空）
     * @return 浏览历史DTO
     */
    BrowseHistoryDTO recordBrowse(Long contentId, Long userId);

    /**
     * 根据ID获取浏览历史
     *
     * @param id 主键ID
     * @return 浏览历史DTO对象
     */
    BrowseHistoryDTO getById(Long id);

    /**
     * 根据ID删除浏览历史
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 分页查询浏览历史
     *
     * @param pageReq 分页请求参数
     * @return 分页结果
     */
    PageResult<BrowseHistoryDTO> page(BrowseHistoryPageReq pageReq);

    /**
     * 获取内容的浏览次数
     *
     * @param contentId 内容ID
     * @return 浏览总次数
     */
    Integer getContentBrowseCount(Long contentId);

    /**
     * 清空指定用户的所有浏览历史
     *
     * @param userId 用户ID
     * @param contentType 内容类型（可选，为空则清空所有类型）
     * @return 清空的记录数
     */
    Integer clearByUserId(ClearReq clearReq);
}
