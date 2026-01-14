package com.dzy.river.chart.luo.writ.domain.convert;

import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 数据内容表 转换接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper(componentModel = "spring")
public interface ContentConvert {

    ContentConvert INSTANCE = Mappers.getMapper(ContentConvert.class);

    /**
     * Content 转 ContentDTO
     *
     * @param content 数据内容表实体
     * @return 数据内容表DTO
     */
    @Mapping(target = "imageUrlList", expression = "java(convertToImageUrlList(content.getImageUrl()))")
    ContentDTO toContentDTO(Content content);

    /**
     * ContentDTO 转 Content
     *
     * @param contentDTO 数据内容表DTO
     * @return 数据内容表实体
     */
    Content toContent(ContentDTO contentDTO);

    /**
     * Content 列表转 ContentDTO 列表
     *
     * @param contentList 数据内容表实体列表
     * @return 数据内容表DTO列表
     */
    List<ContentDTO> toContentDTOList(List<Content> contentList);

    /**
     * ContentDTO 列表转 Content 列表
     *
     * @param contentDTOList 数据内容表DTO列表
     * @return 数据内容表实体列表
     */
    List<Content> toContentList(List<ContentDTO> contentDTOList);


    default List<String> convertToImageUrlList(String imageUrl) {
        if (StringUtils.hasText(imageUrl)) {
            return Arrays.stream(imageUrl.split(",")).toList();
        }
        return new ArrayList<>();
    }

}