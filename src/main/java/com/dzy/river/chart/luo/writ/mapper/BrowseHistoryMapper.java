package com.dzy.river.chart.luo.writ.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.river.chart.luo.writ.domain.entity.BrowseHistory;
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
}
