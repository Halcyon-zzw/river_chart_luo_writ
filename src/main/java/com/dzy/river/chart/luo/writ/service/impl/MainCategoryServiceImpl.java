package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.MainCategory;
import com.dzy.river.chart.luo.writ.domain.dto.MainCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.convert.MainCategoryConvert;
import com.dzy.river.chart.luo.writ.dao.MainCategoryDao;
import com.dzy.river.chart.luo.writ.service.MainCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Override
    public MainCategoryDTO getById(Long id) {
        MainCategory mainCategory = mainCategoryDao.getById(id);
        return mainCategory != null ? mainCategoryConvert.toMainCategoryDTO(mainCategory) : null;
    }

    @Override
    public MainCategoryDTO save(MainCategoryDTO mainCategoryDTO) {
        MainCategory mainCategory = mainCategoryConvert.toMainCategory(mainCategoryDTO);
        boolean success = mainCategoryDao.save(mainCategory);
        return success ? mainCategoryConvert.toMainCategoryDTO(mainCategory) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return mainCategoryDao.removeById(id);
    }

    @Override
    public MainCategoryDTO updateById(Long id, MainCategoryDTO mainCategoryDTO) {
        mainCategoryDTO.setId(id);
        MainCategory mainCategory = mainCategoryConvert.toMainCategory(mainCategoryDTO);
        boolean success = mainCategoryDao.updateById(mainCategory);
        return success ? mainCategoryConvert.toMainCategoryDTO(mainCategory) : null;
    }

}
