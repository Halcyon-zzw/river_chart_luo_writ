package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 标签表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface TagConvert {

    TagConvert INSTANCE = Mappers.getMapper(TagConvert.class);

    /**
     * Tag 转 TagDTO
     *
     * @param tag 标签表实体
     * @return 标签表DTO
     */
    TagDTO toTagDTO(Tag tag);

    /**
     * TagDTO 转 Tag
     *
     * @param tagDTO 标签表DTO
     * @return 标签表实体
     */
    Tag toTag(TagDTO tagDTO);

    /**
     * Tag 列表转 TagDTO 列表
     *
     * @param tagList 标签表实体列表
     * @return 标签表DTO列表
     */
    List<TagDTO> toTagDTOList(List<Tag> tagList);

    /**
     * TagDTO 列表转 Tag 列表
     *
     * @param tagDTOList 标签表DTO列表
     * @return 标签表实体列表
     */
    List<Tag> toTagList(List<TagDTO> tagDTOList);

}