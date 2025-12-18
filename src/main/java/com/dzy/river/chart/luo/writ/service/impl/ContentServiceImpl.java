package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.domain.convert.ContentConvert;
import com.dzy.river.chart.luo.writ.dao.ContentDao;
import com.dzy.river.chart.luo.writ.service.ContentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

}
