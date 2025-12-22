package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategory;
import com.dzy.river.chart.luo.writ.mapper.SubCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>
    * 小分类表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class SubCategoryDao extends ServiceImpl<SubCategoryMapper, SubCategory> {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    /**
     * 批量统计主分类对应的子分类数量
     *
     * @param mainCategoryIds 主分类ID列表
     * @return Map<主分类ID, 子分类数量>
     */
    public Map<Long, Long> countByMainCategoryIds(List<Long> mainCategoryIds) {
        if (mainCategoryIds == null || mainCategoryIds.isEmpty()) {
            return new HashMap<>();
        }

        List<Map<String, Object>> resultList = subCategoryMapper.countByMainCategoryIds(mainCategoryIds);

        Map<Long, Long> countMap = new HashMap<>();
        for (Map<String, Object> map : resultList) {
            Long mainCategoryId = ((Number) map.get("main_category_id")).longValue();
            Long count = ((Number) map.get("count")).longValue();
            countMap.put(mainCategoryId, count);
        }

        return countMap;
    }

}