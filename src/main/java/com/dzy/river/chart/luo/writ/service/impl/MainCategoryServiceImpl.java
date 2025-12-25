package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.MainCategoryTagDao;
import com.dzy.river.chart.luo.writ.dao.SubCategoryDao;
import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.entity.MainCategory;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.convert.MainCategoryConvert;
import com.dzy.river.chart.luo.writ.dao.MainCategoryDao;
import com.dzy.river.chart.luo.writ.domain.entity.MainCategoryTag;
import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.domain.req.MainCategoryPageReq;
import com.dzy.river.chart.luo.writ.service.MainCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 主分类表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class MainCategoryServiceImpl implements MainCategoryService {

    @Autowired
    private MainCategoryDao mainCategoryDao;

    @Autowired
    private MainCategoryConvert mainCategoryConvert;

    @Autowired
    private MainCategoryTagDao mainCategoryTagDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagConvert tagConvert;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Override
    public MainCategoryDTO getById(Long id) {
        MainCategory mainCategory = mainCategoryDao.getById(id);
        if (mainCategory == null) {
            return null;
        }

        // 转换为DTO
        MainCategoryDTO mainCategoryDTO = mainCategoryConvert.toMainCategoryDTO(mainCategory);

        // 查询并填充标签列表
        LambdaQueryWrapper<MainCategoryTag> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(MainCategoryTag::getMainCategoryId, id);
        List<MainCategoryTag> mainCategoryTags = mainCategoryTagDao.list(tagWrapper);

        if (!CollectionUtils.isEmpty(mainCategoryTags)) {
            List<Long> tagIds = mainCategoryTags.stream()
                    .map(MainCategoryTag::getTagId)
                    .collect(Collectors.toList());
            List<Tag> tags = tagDao.listByIds(tagIds);
            mainCategoryDTO.setTagDTOList(tagConvert.toTagDTOList(tags));
        } else {
            mainCategoryDTO.setTagDTOList(new ArrayList<>());
        }

        return mainCategoryDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MainCategoryDTO save(MainCategoryDTO mainCategoryDTO) {
        // 1. 保存主分类
        MainCategory mainCategory = mainCategoryConvert.toMainCategory(mainCategoryDTO);
        boolean success = mainCategoryDao.save(mainCategory);
        if (!success) {
            return null;
        }

        // 2. 关联标签（如果有）
        if (mainCategoryDTO.getTagIdList() != null) {
            associateTags(mainCategory.getId(), mainCategoryDTO.getTagIdList());
        }

        // 3. 返回结果（包含标签信息）
        MainCategoryDTO result = mainCategoryConvert.toMainCategoryDTO(mainCategory);
        if (!CollectionUtils.isEmpty(mainCategoryDTO.getTagIdList())) {
            // 查询并填充标签信息
            List<Tag> tags = tagDao.listByIds(mainCategoryDTO.getTagIdList());
            result.setTagDTOList(tagConvert.toTagDTOList(tags));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        // 1. 删除标签关联
        LambdaQueryWrapper<MainCategoryTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(MainCategoryTag::getMainCategoryId, id);
        mainCategoryTagDao.remove(deleteWrapper);

        // 2. 删除主分类
        return mainCategoryDao.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MainCategoryDTO updateById(Long id, MainCategoryDTO mainCategoryDTO) {
        // 1. 更新主分类
        mainCategoryDTO.setId(id);
        MainCategory mainCategory = mainCategoryConvert.toMainCategory(mainCategoryDTO);
        boolean success = mainCategoryDao.updateById(mainCategory);
        if (!success) {
            return null;
        }

        // 2. 更新标签关联（如果有）
        if (mainCategoryDTO.getTagIdList() != null) {
            associateTags(id, mainCategoryDTO.getTagIdList());
        }

        // 3. 返回结果（包含标签信息）
        MainCategoryDTO result = mainCategoryConvert.toMainCategoryDTO(mainCategory);
        if (!CollectionUtils.isEmpty(mainCategoryDTO.getTagIdList())) {
            // 查询并填充标签信息
            List<Tag> tags = tagDao.listByIds(mainCategoryDTO.getTagIdList());
            result.setTagDTOList(tagConvert.toTagDTOList(tags));
        }
        return result;
    }

    @Override
    public PageResult<MainCategoryDTO> page(MainCategoryPageReq mainCategoryPageReq) {
        // 1. 构建查询条件
        LambdaQueryWrapper<MainCategory> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(mainCategoryPageReq.getName())) {
            queryWrapper.like(MainCategory::getName, mainCategoryPageReq.getName());
        }
        queryWrapper.orderByDesc(MainCategory::getCreateTime);

        // 2. 分页查询主数据
        Page<MainCategory> page = mainCategoryDao.page(mainCategoryPageReq.convertToPage(), queryWrapper);
        List<MainCategory> mainCategories = page.getRecords();

        // 3. 转换为DTO
        List<MainCategoryDTO> mainCategoryDTOList = mainCategories.stream()
                .map(mainCategoryConvert::toMainCategoryDTO)
                .collect(Collectors.toList());

        // 4. 如果主数据为空，直接返回
        if (CollectionUtils.isEmpty(mainCategoryDTOList)) {
            return new PageResult<>(page, mainCategoryDTOList);
        }

        // 5. 批量查询关联的标签
        List<Long> mainCategoryIds = mainCategoryDTOList.stream()
                .map(MainCategoryDTO::getId)
                .collect(Collectors.toList());

        // 6. 查询主分类标签关联关系
        LambdaQueryWrapper<MainCategoryTag> tagRelationWrapper = new LambdaQueryWrapper<>();
        tagRelationWrapper.in(MainCategoryTag::getMainCategoryId, mainCategoryIds);
        List<MainCategoryTag> mainCategoryTags = mainCategoryTagDao.list(tagRelationWrapper);

        // 7. 如果没有标签关联，直接返回
        if (!CollectionUtils.isEmpty(mainCategoryTags)) {
            fillTag(mainCategoryDTOList, mainCategoryTags);
        }

        // 12. 批量统计子分类数量
        Map<Long, Long> subCategoryCountMap = subCategoryDao.countByMainCategoryIds(mainCategoryIds);

        // 13. 设置每个主分类的子分类数量
        for (MainCategoryDTO mainCategoryDTO : mainCategoryDTOList) {
            Long count = subCategoryCountMap.getOrDefault(mainCategoryDTO.getId(), 0L);
            mainCategoryDTO.setSubCategorySize(count.intValue());
        }

        // 14. 返回分页结果
        return new PageResult<>(page, mainCategoryDTOList);
    }

    private void fillTag(List<MainCategoryDTO> mainCategoryDTOList, List<MainCategoryTag> mainCategoryTags) {
        // 8. 提取标签ID并批量查询标签
        List<Long> tagIds = mainCategoryTags.stream()
                .map(MainCategoryTag::getTagId)
                .distinct()
                .collect(Collectors.toList());

        List<Tag> tags = tagDao.listByIds(tagIds);
        List<TagDTO> tagDTOList = tagConvert.toTagDTOList(tags);

        // 9. 构建标签ID到标签DTO的映射
        Map<Long, TagDTO> tagDTOMap = tagDTOList.stream()
                .collect(Collectors.toMap(TagDTO::getId, tagDTO -> tagDTO));

        // 10. 构建主分类ID到标签列表的映射
        Map<Long, List<TagDTO>> mainCategoryTagMap = new HashMap<>();
        for (MainCategoryTag mainCategoryTag : mainCategoryTags) {
            TagDTO tagDTO = tagDTOMap.get(mainCategoryTag.getTagId());
            if (tagDTO != null) {
                mainCategoryTagMap.computeIfAbsent(mainCategoryTag.getMainCategoryId(), k -> new ArrayList<>())
                        .add(tagDTO);
            }
        }

        // 11. 设置每个主分类的标签列表
        for (MainCategoryDTO mainCategoryDTO : mainCategoryDTOList) {
            List<TagDTO> categoryTags = mainCategoryTagMap.get(mainCategoryDTO.getId());
            mainCategoryDTO.setTagDTOList(categoryTags != null ? categoryTags : new ArrayList<>());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean associateTags(Long mainCategoryId, List<Long> tagIds) {
        // 1. 先物理删除该主分类下所有已逻辑删除的记录，避免后续插入时唯一键冲突
        mainCategoryTagDao.getBaseMapper().delete(
                new LambdaQueryWrapper<MainCategoryTag>()
                        .eq(MainCategoryTag::getMainCategoryId, mainCategoryId)
                        .eq(MainCategoryTag::getIsDeleted, 1)
        );

        // 2. 删除现有的标签关联（逻辑删除）
        LambdaQueryWrapper<MainCategoryTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(MainCategoryTag::getMainCategoryId, mainCategoryId);
        mainCategoryTagDao.remove(deleteWrapper);

        // 3. 如果标签列表为空，直接返回
        if (CollectionUtils.isEmpty(tagIds)) {
            return true;
        }

        // 4. 创建新的标签关联
        List<MainCategoryTag> mainCategoryTags = tagIds.stream()
                .map(tagId -> {
                    MainCategoryTag mainCategoryTag = new MainCategoryTag();
                    mainCategoryTag.setMainCategoryId(mainCategoryId);
                    mainCategoryTag.setTagId(tagId);
                    return mainCategoryTag;
                })
                .collect(Collectors.toList());

        return mainCategoryTagDao.saveBatch(mainCategoryTags);
    }

}
