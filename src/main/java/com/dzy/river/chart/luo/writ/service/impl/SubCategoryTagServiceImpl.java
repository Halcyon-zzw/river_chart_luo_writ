package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dzy.river.chart.luo.writ.domain.entity.SubCategoryTag;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryTagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.SubCategoryTagConvert;
import com.dzy.river.chart.luo.writ.dao.SubCategoryTagDao;
import com.dzy.river.chart.luo.writ.service.SubCategoryTagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 小分类标签关联表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class SubCategoryTagServiceImpl implements SubCategoryTagService {

    @Autowired
    private SubCategoryTagDao subCategoryTagDao;

    @Autowired
    private SubCategoryTagConvert subCategoryTagConvert;

    @Override
    public SubCategoryTagDTO getById(Long id) {
        SubCategoryTag subCategoryTag = subCategoryTagDao.getById(id);
        return subCategoryTag != null ? subCategoryTagConvert.toSubCategoryTagDTO(subCategoryTag) : null;
    }

    @Override
    public SubCategoryTagDTO save(SubCategoryTagDTO subCategoryTagDTO) {
        SubCategoryTag subCategoryTag = subCategoryTagConvert.toSubCategoryTag(subCategoryTagDTO);
        boolean success = subCategoryTagDao.save(subCategoryTag);
        return success ? subCategoryTagConvert.toSubCategoryTagDTO(subCategoryTag) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return subCategoryTagDao.removeById(id);
    }

    @Override
    public SubCategoryTagDTO updateById(Long id, SubCategoryTagDTO subCategoryTagDTO) {
        subCategoryTagDTO.setId(id);
        SubCategoryTag subCategoryTag = subCategoryTagConvert.toSubCategoryTag(subCategoryTagDTO);
        boolean success = subCategoryTagDao.updateById(subCategoryTag);
        return success ? subCategoryTagConvert.toSubCategoryTagDTO(subCategoryTag) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchLinkTags(Long subCategoryId, List<Long> tagIds) {
        // 1. 删除该子分类的所有旧关联记录
        LambdaQueryWrapper<SubCategoryTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SubCategoryTag::getSubCategoryId, subCategoryId);
        subCategoryTagDao.remove(deleteWrapper);

        // 2. 如果标签列表为空，直接返回
        if (tagIds == null || tagIds.isEmpty()) {
            return 0;
        }

        // 3. 批量插入新的关联记录
        List<SubCategoryTag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            SubCategoryTag tag = new SubCategoryTag();
            tag.setSubCategoryId(subCategoryId);
            tag.setTagId(tagId);
            tags.add(tag);
        }

        // 4. 批量保存
        boolean success = subCategoryTagDao.saveBatch(tags);
        return success ? tags.size() : 0;
    }

}
