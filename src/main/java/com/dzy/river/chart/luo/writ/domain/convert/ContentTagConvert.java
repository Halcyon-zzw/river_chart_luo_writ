package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.ContentTag;
import com.dzy.river.chart.luo.writ.domain.dto.ContentTagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 内容标签关联表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface ContentTagConvert {

    ContentTagConvert INSTANCE = Mappers.getMapper(ContentTagConvert.class);

    /**
     * ContentTag 转 ContentTagDTO
     *
     * @param contentTag 内容标签关联表实体
     * @return 内容标签关联表DTO
     */
    ContentTagDTO toContentTagDTO(ContentTag contentTag);

    /**
     * ContentTagDTO 转 ContentTag
     *
     * @param contentTagDTO 内容标签关联表DTO
     * @return 内容标签关联表实体
     */
    ContentTag toContentTag(ContentTagDTO contentTagDTO);

    /**
     * ContentTag 列表转 ContentTagDTO 列表
     *
     * @param contentTagList 内容标签关联表实体列表
     * @return 内容标签关联表DTO列表
     */
    List<ContentTagDTO> toContentTagDTOList(List<ContentTag> contentTagList);

    /**
     * ContentTagDTO 列表转 ContentTag 列表
     *
     * @param contentTagDTOList 内容标签关联表DTO列表
     * @return 内容标签关联表实体列表
     */
    List<ContentTag> toContentTagList(List<ContentTagDTO> contentTagDTOList);

}