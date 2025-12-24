package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dzy.river.chart.luo.writ.domain.entity.MainCategoryTag;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryTagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.MainCategoryTagConvert;
import com.dzy.river.chart.luo.writ.dao.MainCategoryTagDao;
import com.dzy.river.chart.luo.writ.service.MainCategoryTagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 主分类标签关联表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class MainCategoryTagServiceImpl implements MainCategoryTagService {

    @Autowired
    private MainCategoryTagDao mainCategoryTagDao;

    @Autowired
    private MainCategoryTagConvert mainCategoryTagConvert;

    @Override
    public MainCategoryTagDTO getById(Long id) {
        MainCategoryTag mainCategoryTag = mainCategoryTagDao.getById(id);
        return mainCategoryTag != null ? mainCategoryTagConvert.toMainCategoryTagDTO(mainCategoryTag) : null;
    }

    @Override
    public MainCategoryTagDTO save(MainCategoryTagDTO mainCategoryTagDTO) {
        MainCategoryTag mainCategoryTag = mainCategoryTagConvert.toMainCategoryTag(mainCategoryTagDTO);
        boolean success = mainCategoryTagDao.save(mainCategoryTag);
        return success ? mainCategoryTagConvert.toMainCategoryTagDTO(mainCategoryTag) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return mainCategoryTagDao.removeById(id);
    }

    @Override
    public MainCategoryTagDTO updateById(Long id, MainCategoryTagDTO mainCategoryTagDTO) {
        mainCategoryTagDTO.setId(id);
        MainCategoryTag mainCategoryTag = mainCategoryTagConvert.toMainCategoryTag(mainCategoryTagDTO);
        boolean success = mainCategoryTagDao.updateById(mainCategoryTag);
        return success ? mainCategoryTagConvert.toMainCategoryTagDTO(mainCategoryTag) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchLinkTags(Long mainCategoryId, List<Long> tagIds) {
        // 1. 删除该主分类的所有旧关联记录
        LambdaQueryWrapper<MainCategoryTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(MainCategoryTag::getMainCategoryId, mainCategoryId);
        mainCategoryTagDao.remove(deleteWrapper);

        // 2. 如果标签列表为空，直接返回
        if (tagIds == null || tagIds.isEmpty()) {
            return 0;
        }

        // 3. 批量插入新的关联记录
        List<MainCategoryTag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            MainCategoryTag tag = new MainCategoryTag();
            tag.setMainCategoryId(mainCategoryId);
            tag.setTagId(tagId);
            tags.add(tag);
        }

        // 4. 批量保存
        boolean success = mainCategoryTagDao.saveBatch(tags);
        return success ? tags.size() : 0;
    }

}
