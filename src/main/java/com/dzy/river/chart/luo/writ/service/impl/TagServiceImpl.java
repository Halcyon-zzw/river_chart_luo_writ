package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagConvert tagConvert;

    @Override
    public TagDTO getById(Long id) {
        Tag tag = tagDao.getById(id);
        return tag != null ? tagConvert.toTagDTO(tag) : null;
    }

    @Override
    public TagDTO save(TagDTO tagDTO) {
        Tag tag = tagConvert.toTag(tagDTO);
        boolean success = tagDao.save(tag);
        return success ? tagConvert.toTagDTO(tag) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return tagDao.removeById(id);
    }

    @Override
    public TagDTO updateById(Long id, TagDTO tagDTO) {
        tagDTO.setId(id);
        Tag tag = tagConvert.toTag(tagDTO);
        boolean success = tagDao.updateById(tag);
        return success ? tagConvert.toTagDTO(tag) : null;
    }

}
