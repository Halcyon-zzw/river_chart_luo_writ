package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.ContentTagDao;
import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.domain.convert.ContentConvert;
import com.dzy.river.chart.luo.writ.dao.ContentDao;
import com.dzy.river.chart.luo.writ.domain.entity.ContentTag;
import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.domain.req.ContentPageReq;
import com.dzy.river.chart.luo.writ.service.ContentService;
import com.dzy.river.chart.luo.writ.service.MainCategoryService;
import com.dzy.river.chart.luo.writ.service.SubCategoryService;
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

    @Autowired
    private MainCategoryService mainCategoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Override
    public ContentDTO getById(Long id) {
        Content content = contentDao.getById(id);
        if (content == null) {
            return null;
        }

        // 转换为DTO
        ContentDTO contentDTO = contentConvert.toContentDTO(content);

        // 查询并填充标签列表
        LambdaQueryWrapper<ContentTag> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(ContentTag::getContentId, id);
        List<ContentTag> contentTags = contentTagDao.list(tagWrapper);

        if (!CollectionUtils.isEmpty(contentTags)) {
            List<Long> tagIds = contentTags.stream()
                    .map(ContentTag::getTagId)
                    .collect(Collectors.toList());
            List<Tag> tags = tagDao.listByIds(tagIds);
            contentDTO.setTagDTOList(tagConvert.toTagDTOList(tags));
        } else {
            contentDTO.setTagDTOList(new ArrayList<>());
        }

        return contentDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentDTO save(ContentDTO contentDTO) {
        // 1. 保存内容
        Content content = contentConvert.toContent(contentDTO);
        boolean success = contentDao.save(content);
        if (!success) {
            return null;
        }

        // 2. 关联标签（如果有）
        if (contentDTO.getTagIdList() != null) {
            associateTags(content.getId(), contentDTO.getTagIdList());
        }

        // 3. 返回结果（包含标签信息）
        ContentDTO result = contentConvert.toContentDTO(content);
        if (!CollectionUtils.isEmpty(contentDTO.getTagIdList())) {
            // 查询并填充标签信息
            List<Tag> tags = tagDao.listByIds(contentDTO.getTagIdList());
            result.setTagDTOList(tagConvert.toTagDTOList(tags));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        // 1. 删除标签关联
        LambdaQueryWrapper<ContentTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ContentTag::getContentId, id);
        contentTagDao.remove(deleteWrapper);

        // 2. 删除内容
        return contentDao.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentDTO updateById(Long id, ContentDTO contentDTO) {
        // 1. 更新内容
        contentDTO.setId(id);
        Content content = contentConvert.toContent(contentDTO);
        boolean success = contentDao.updateById(content);
        if (!success) {
            return null;
        }

        // 2. 更新标签关联（如果有）
        if (contentDTO.getTagIdList() != null) {
            associateTags(id, contentDTO.getTagIdList());
        }

        // 3. 返回结果（包含标签信息）
        ContentDTO result = contentConvert.toContentDTO(content);
        if (!CollectionUtils.isEmpty(contentDTO.getTagIdList())) {
            // 查询并填充标签信息
            List<Tag> tags = tagDao.listByIds(contentDTO.getTagIdList());
            result.setTagDTOList(tagConvert.toTagDTOList(tags));
        }
        return result;
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
        // 1. 先物理删除该内容下所有已逻辑删除的记录，避免后续插入时唯一键冲突
        contentTagDao.getBaseMapper().delete(
                new LambdaQueryWrapper<ContentTag>()
                        .eq(ContentTag::getContentId, contentId)
                        .eq(ContentTag::getIsDeleted, 1)
        );

        // 2. 删除现有的标签关联（逻辑删除）
        LambdaQueryWrapper<ContentTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ContentTag::getContentId, contentId);
        contentTagDao.remove(deleteWrapper);

        // 3. 如果标签列表为空，直接返回
        if (CollectionUtils.isEmpty(tagIds)) {
            return true;
        }

        // 4. 创建新的标签关联
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentDTO createWithCategories(MainCategoryDTO mainCategoryDTO, SubCategoryDTO subCategoryDTO, ContentDTO contentDTO) {
        // 1. 创建主分类及关联标签（忽略传入的ID）
        mainCategoryDTO.setId(null);
        MainCategoryDTO createdMainCategory = mainCategoryService.save(mainCategoryDTO);
        if (createdMainCategory == null) {
            throw new RuntimeException("创建主分类失败");
        }

        // 2. 创建子分类及关联标签（忽略传入的ID，设置主分类ID）
        subCategoryDTO.setId(null);
        subCategoryDTO.setMainCategoryId(createdMainCategory.getId());
        SubCategoryDTO createdSubCategory = subCategoryService.save(subCategoryDTO);
        if (createdSubCategory == null) {
            throw new RuntimeException("创建子分类失败");
        }

        // 3. 创建内容及关联标签（忽略传入的ID，设置子分类ID）
        contentDTO.setId(null);
        contentDTO.setSubCategoryId(createdSubCategory.getId());
        ContentDTO createdContent = save(contentDTO);
        if (createdContent == null) {
            throw new RuntimeException("创建内容失败");
        }

        // 4. 返回完整的内容信息（包含标签列表）
        return getById(createdContent.getId());
    }

}
