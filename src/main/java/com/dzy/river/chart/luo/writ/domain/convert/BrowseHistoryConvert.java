package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.entity.BrowseHistory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <p>
 * 浏览历史表 Convert
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
@Mapper(componentModel = "spring")
public interface BrowseHistoryConvert {

    /**
     * Entity 转 DTO
     */
    BrowseHistoryDTO toBrowseHistoryDTO(BrowseHistory entity);

    /**
     * DTO 转 Entity
     */
    BrowseHistory toBrowseHistory(BrowseHistoryDTO dto);

    /**
     * Entity List 转 DTO List
     */
    List<BrowseHistoryDTO> toBrowseHistoryDTOList(List<BrowseHistory> entities);
}
