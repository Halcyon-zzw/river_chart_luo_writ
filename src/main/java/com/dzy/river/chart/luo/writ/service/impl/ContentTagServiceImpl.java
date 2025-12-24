package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dzy.river.chart.luo.writ.domain.entity.ContentTag;
import com.dzy.river.chart.luo.writ.domain.dto.ContentTagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.ContentTagConvert;
import com.dzy.river.chart.luo.writ.dao.ContentTagDao;
import com.dzy.river.chart.luo.writ.service.ContentTagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 内容标签关联表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class ContentTagServiceImpl implements ContentTagService {

    @Autowired
    private ContentTagDao contentTagDao;

    @Autowired
    private ContentTagConvert contentTagConvert;

    @Override
    public ContentTagDTO getById(Long id) {
        ContentTag contentTag = contentTagDao.getById(id);
        return contentTag != null ? contentTagConvert.toContentTagDTO(contentTag) : null;
    }

    @Override
    public ContentTagDTO save(ContentTagDTO contentTagDTO) {
        ContentTag contentTag = contentTagConvert.toContentTag(contentTagDTO);
        boolean success = contentTagDao.save(contentTag);
        return success ? contentTagConvert.toContentTagDTO(contentTag) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return contentTagDao.removeById(id);
    }

    @Override
    public ContentTagDTO updateById(Long id, ContentTagDTO contentTagDTO) {
        contentTagDTO.setId(id);
        ContentTag contentTag = contentTagConvert.toContentTag(contentTagDTO);
        boolean success = contentTagDao.updateById(contentTag);
        return success ? contentTagConvert.toContentTagDTO(contentTag) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchLinkTags(Long contentId, List<Long> tagIds) {
        // 1. 删除该内容的所有旧关联记录
        LambdaQueryWrapper<ContentTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ContentTag::getContentId, contentId);
        contentTagDao.remove(deleteWrapper);

        // 2. 如果标签列表为空，直接返回
        if (tagIds == null || tagIds.isEmpty()) {
            return 0;
        }

        // 3. 批量插入新的关联记录
        List<ContentTag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            ContentTag tag = new ContentTag();
            tag.setContentId(contentId);
            tag.setTagId(tagId);
            tags.add(tag);
        }

        // 4. 批量保存
        boolean success = contentTagDao.saveBatch(tags);
        return success ? tags.size() : 0;
    }

}
