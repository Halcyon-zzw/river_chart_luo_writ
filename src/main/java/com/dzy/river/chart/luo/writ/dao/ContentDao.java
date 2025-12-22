package com.dzy.river.chart.luo.writ.dao;

import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.mapper.ContentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>
    * 数据内容表 Dao类
    * </p>
*
* @author zhuzhiwei
* @since 2025-12-18
*/
@Repository
public class ContentDao extends ServiceImpl<ContentMapper, Content> {

    @Autowired
    private ContentMapper contentMapper;

    /**
     * 批量统计子分类对应的内容数量
     *
     * @param subCategoryIds 子分类ID列表
     * @return Map<子分类ID, 内容数量>
     */
    public Map<Long, Long> countBySubCategoryIds(List<Long> subCategoryIds) {
        if (subCategoryIds == null || subCategoryIds.isEmpty()) {
            return new HashMap<>();
        }

        List<Map<String, Object>> resultList = contentMapper.countBySubCategoryIds(subCategoryIds);

        Map<Long, Long> countMap = new HashMap<>();
        for (Map<String, Object> map : resultList) {
            Long subCategoryId = ((Number) map.get("sub_category_id")).longValue();
            Long count = ((Number) map.get("count")).longValue();
            countMap.put(subCategoryId, count);
        }

        return countMap;
    }

}