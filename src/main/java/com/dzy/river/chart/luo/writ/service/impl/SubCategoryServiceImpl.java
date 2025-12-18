package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.SubCategoryTagDao;
import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.entity.SubCategory;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.convert.SubCategoryConvert;
import com.dzy.river.chart.luo.writ.dao.SubCategoryDao;
import com.dzy.river.chart.luo.writ.domain.entity.SubCategoryTag;
import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.domain.req.SubCategoryPageReq;
import com.dzy.river.chart.luo.writ.service.SubCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 小分类表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Autowired
    private SubCategoryConvert subCategoryConvert;

    @Autowired
    private SubCategoryTagDao subCategoryTagDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagConvert tagConvert;

    @Override
    public SubCategoryDTO getById(Long id) {
        SubCategory subCategory = subCategoryDao.getById(id);
        return subCategory != null ? subCategoryConvert.toSubCategoryDTO(subCategory) : null;
    }

    @Override
    public SubCategoryDTO save(SubCategoryDTO subCategoryDTO) {
        SubCategory subCategory = subCategoryConvert.toSubCategory(subCategoryDTO);
        boolean success = subCategoryDao.save(subCategory);
        return success ? subCategoryConvert.toSubCategoryDTO(subCategory) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return subCategoryDao.removeById(id);
    }

    @Override
    public SubCategoryDTO updateById(Long id, SubCategoryDTO subCategoryDTO) {
        subCategoryDTO.setId(id);
        SubCategory subCategory = subCategoryConvert.toSubCategory(subCategoryDTO);
        boolean success = subCategoryDao.updateById(subCategory);
        return success ? subCategoryConvert.toSubCategoryDTO(subCategory) : null;
    }

    @Override
    public PageResult page(SubCategoryPageReq subCategoryPageReq) {
        // 1. 构建查询条件
        LambdaQueryWrapper<SubCategory> queryWrapper = new LambdaQueryWrapper<>();
        if (subCategoryPageReq.getMainCategoryId() != null) {
            queryWrapper.eq(SubCategory::getMainCategoryId, subCategoryPageReq.getMainCategoryId());
        }
        queryWrapper.orderByDesc(SubCategory::getCreatedTime);

        // 2. 分页查询主数据
        Page<SubCategory> page = subCategoryDao.page(subCategoryPageReq.convertToPage(), queryWrapper);
        List<SubCategory> subCategories = page.getRecords();

        // 3. 转换为DTO
        List<SubCategoryDTO> subCategoryDTOList = subCategories.stream()
                .map(subCategoryConvert::toSubCategoryDTO)
                .collect(Collectors.toList());

        // 4. 如果主数据为空，直接返回
        if (CollectionUtils.isEmpty(subCategoryDTOList)) {
            return new PageResult(page, subCategoryDTOList);
        }

        // 5. 批量查询关联的标签
        List<Long> subCategoryIds = subCategoryDTOList.stream()
                .map(SubCategoryDTO::getId)
                .collect(Collectors.toList());

        // 6. 查询小分类标签关联关系
        LambdaQueryWrapper<SubCategoryTag> tagRelationWrapper = new LambdaQueryWrapper<>();
        tagRelationWrapper.in(SubCategoryTag::getSubCategoryId, subCategoryIds);
        List<SubCategoryTag> subCategoryTags = subCategoryTagDao.list(tagRelationWrapper);

        // 7. 如果没有标签关联，直接返回
        if (CollectionUtils.isEmpty(subCategoryTags)) {
            return new PageResult(page, subCategoryDTOList);
        }

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

        // 10. 构建小分类ID到标签列表的映射
        Map<Long, List<TagDTO>> subCategoryTagMap = new HashMap<>();
        for (SubCategoryTag subCategoryTag : subCategoryTags) {
            TagDTO tagDTO = tagDTOMap.get(subCategoryTag.getTagId());
            if (tagDTO != null) {
                subCategoryTagMap.computeIfAbsent(subCategoryTag.getSubCategoryId(), k -> new ArrayList<>())
                        .add(tagDTO);
            }
        }

        // 11. 设置每个小分类的标签列表
        for (SubCategoryDTO subCategoryDTO : subCategoryDTOList) {
            List<TagDTO> categoryTags = subCategoryTagMap.get(subCategoryDTO.getId());
            subCategoryDTO.setTagDTOList(categoryTags != null ? categoryTags : new ArrayList<>());
        }

        // 12. 返回分页结果
        return new PageResult(page, subCategoryDTOList);
    }

}
