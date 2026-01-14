package com.dzy.river.chart.luo.writ.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.entity.BrowseHistory;
import com.dzy.river.chart.luo.writ.domain.req.BrowseHistoryPageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 浏览历史表 Mapper 接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {

    /**
     * 查询用户对某个内容的浏览记录（包括已删除的记录）
     *
     * @param contentId 内容ID
     * @param userId 用户ID
     * @return 浏览历史记录
     */
    BrowseHistory selectByContentIdAndUserId(@Param("contentId") Long contentId, @Param("userId") Long userId);

    /**
     * 分页查询浏览历史（关联查询内容标题）
     *
     * @param page 分页对象
     * @param req 查询条件
     * @return 浏览历史分页结果
     */
    IPage<BrowseHistoryDTO> selectPageWithContentTitle(Page<BrowseHistoryDTO> page, @Param("req") BrowseHistoryPageReq req);

    /**
     * 分页查询浏览历史（不使用 JOIN）
     *
     * @param page 分页对象
     * @param req 查询条件
     * @return 浏览历史分页结果
     */
    IPage<BrowseHistory> selectPageWithoutJoin(Page<BrowseHistory> page, @Param("req") BrowseHistoryPageReq req);
}
