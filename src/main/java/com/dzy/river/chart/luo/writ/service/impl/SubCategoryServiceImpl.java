package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategory;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryDTO;
import com.dzy.river.chart.luo.writ.domain.convert.SubCategoryConvert;
import com.dzy.river.chart.luo.writ.dao.SubCategoryDao;
import com.dzy.river.chart.luo.writ.service.SubCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

}
