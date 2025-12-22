package com.dzy.river.chart.luo.writ.manager;

import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.entity.SubCategoryTag;
import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TagManager
 *
 * @author zhuzhiwei
 * @date 2025/12/22 14:32
 */
@Component
public class TagManager {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagConvert tagConvert;

    public Map<Long, TagDTO> getLongTagDTOMap(List<SubCategoryTag> subCategoryTags) {
        // 8. 提取标签ID并批量查询标签
        List<Long> tagIds = subCategoryTags.stream()
                .map(SubCategoryTag::getTagId)
                .distinct()
                .collect(Collectors.toList());

        List<Tag> tags = tagDao.listByIds(tagIds);
        List<TagDTO> tagDTOList = tagConvert.toTagDTOList(tags);

        // 9. 构建标签ID到标签DTO的映射
        Map<Long, TagDTO> tagDTOMap = tagDTOList.stream()
                .collect(Collectors.toMap(TagDTO::getId, tagDTO -> tagDTO));
        return tagDTOMap;
    }
}
