package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategoryTag;
import com.dzy.river.chart.luo.writ.domain.dto.SubCategoryTagDTO;
import com.dzy.river.chart.luo.writ.domain.convert.SubCategoryTagConvert;
import com.dzy.river.chart.luo.writ.dao.SubCategoryTagDao;
import com.dzy.river.chart.luo.writ.service.SubCategoryTagService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

}
