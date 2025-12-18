package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.MainCategoryTag;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryTagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.MainCategoryTagConvert;
import com.dzy.river.chart.luo.writ.dao.MainCategoryTagDao;
import com.dzy.river.chart.luo.writ.service.MainCategoryTagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

}
