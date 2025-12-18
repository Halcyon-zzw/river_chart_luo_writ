package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.ContentTag;
import com.dzy.river.chart.luo.writ.domain.dto.ContentTagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.ContentTagConvert;
import com.dzy.river.chart.luo.writ.dao.ContentTagDao;
import com.dzy.river.chart.luo.writ.service.ContentTagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

}
