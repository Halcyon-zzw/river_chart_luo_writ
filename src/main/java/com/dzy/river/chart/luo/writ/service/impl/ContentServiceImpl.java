package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.ContentTagDao;
import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.domain.convert.ContentConvert;
import com.dzy.river.chart.luo.writ.dao.ContentDao;
import com.dzy.river.chart.luo.writ.domain.entity.ContentTag;
import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.domain.req.ContentPageReq;
import com.dzy.river.chart.luo.writ.service.ContentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据内容表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private ContentConvert contentConvert;

    @Autowired
    private ContentTagDao contentTagDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagConvert tagConvert;

    @Override
    public ContentDTO getById(Long id) {
        Content content = contentDao.getById(id);
        return content != null ? contentConvert.toContentDTO(content) : null;
    }

    @Override
    public ContentDTO save(ContentDTO contentDTO) {
        Content content = contentConvert.toContent(contentDTO);
        boolean success = contentDao.save(content);
        return success ? contentConvert.toContentDTO(content) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return contentDao.removeById(id);
    }

    @Override
    public ContentDTO updateById(Long id, ContentDTO contentDTO) {
        contentDTO.setId(id);
        Content content = contentConvert.toContent(contentDTO);
        boolean success = contentDao.updateById(content);
        return success ? contentConvert.toContentDTO(content) : null;
    }

    @Override
    public PageResult<ContentDTO> page(ContentPageReq contentPageReq) {
        // 1. 构建查询条件
        LambdaQueryWrapper<Content> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Content::getSubCategoryId, contentPageReq.getSubCategoryId());
        if (StringUtils.hasText(contentPageReq.getContentType())) {
            queryWrapper.eq(Content::getContentType, contentPageReq.getContentType());
        }
        if (StringUtils.hasText(contentPageReq.getTitle())) {
            queryWrapper.like(Content::getTitle, contentPageReq.getTitle());
        }
        queryWrapper.orderByDesc(Content::getCreateTime);

        // 2. 分页查询主数据
        Page<Content> page = contentDao.page(contentPageReq.convertToPage(), queryWrapper);
        List<Content> contents = page.getRecords();

        // 3. 转换为DTO
        List<ContentDTO> contentDTOList = contents.stream()
                .map(contentConvert::toContentDTO)
                .collect(Collectors.toList());

        // 4. 如果主数据为空，直接返回
        if (CollectionUtils.isEmpty(contentDTOList)) {
            return new PageResult<>(page, contentDTOList);
        }

        // 5. 批量查询关联的标签
        List<Long> contentIds = contentDTOList.stream()
                .map(ContentDTO::getId)
                .collect(Collectors.toList());

        // 6. 查询内容标签关联关系
        LambdaQueryWrapper<ContentTag> tagRelationWrapper = new LambdaQueryWrapper<>();
        tagRelationWrapper.in(ContentTag::getContentId, contentIds);
        List<ContentTag> contentTags = contentTagDao.list(tagRelationWrapper);

        // 7. 如果没有标签关联，直接返回
        if (CollectionUtils.isEmpty(contentTags)) {
            return new PageResult<>(page, contentDTOList);
        }

        // 8. 提取标签ID并批量查询标签
        List<Long> tagIds = contentTags.stream()
                .map(ContentTag::getTagId)
                .distinct()
                .collect(Collectors.toList());

        List<Tag> tags = tagDao.listByIds(tagIds);
        List<TagDTO> tagDTOList = tagConvert.toTagDTOList(tags);

        // 9. 构建标签ID到标签DTO的映射
        Map<Long, TagDTO> tagDTOMap = tagDTOList.stream()
                .collect(Collectors.toMap(TagDTO::getId, tagDTO -> tagDTO));

        // 10. 构建内容ID到标签列表的映射
        Map<Long, List<TagDTO>> contentTagMap = new HashMap<>();
        for (ContentTag contentTag : contentTags) {
            TagDTO tagDTO = tagDTOMap.get(contentTag.getTagId());
            if (tagDTO != null) {
                contentTagMap.computeIfAbsent(contentTag.getContentId(), k -> new ArrayList<>())
                        .add(tagDTO);
            }
        }

        // 11. 设置每个内容的标签列表
        for (ContentDTO contentDTO : contentDTOList) {
            List<TagDTO> categoryTags = contentTagMap.get(contentDTO.getId());
            contentDTO.setTagDTOList(categoryTags != null ? categoryTags : new ArrayList<>());
        }

        // 12. 返回分页结果
        return new PageResult<>(page, contentDTOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean associateTags(Long contentId, List<Long> tagIds) {
        // 1. 删除现有的标签关联
        LambdaQueryWrapper<ContentTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ContentTag::getContentId, contentId);
        contentTagDao.remove(deleteWrapper);

        // 2. 如果标签列表为空，直接返回
        if (CollectionUtils.isEmpty(tagIds)) {
            return true;
        }

        // 3. 创建新的标签关联
        List<ContentTag> contentTags = tagIds.stream()
                .map(tagId -> {
                    ContentTag contentTag = new ContentTag();
                    contentTag.setContentId(contentId);
                    contentTag.setTagId(tagId);
                    return contentTag;
                })
                .collect(Collectors.toList());

        return contentTagDao.saveBatch(contentTags);
    }

}
