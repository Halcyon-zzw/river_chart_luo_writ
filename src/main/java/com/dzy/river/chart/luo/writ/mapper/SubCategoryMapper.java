package com.dzy.river.chart.luo.writ.mapper;

import com.dzy.river.chart.luo.writ.domain.entity.SubCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小分类表 Mapper 接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper
public interface SubCategoryMapper extends BaseMapper<SubCategory> {

    /**
     * 批量统计主分类对应的子分类数量
     *
     * @param mainCategoryIds 主分类ID列表
     * @return 统计结果，key: main_category_id, value: count
     */
    @Select("<script>" +
            "SELECT main_category_id, COUNT(*) as count " +
            "FROM sub_category " +
            "WHERE main_category_id IN " +
            "<foreach collection='mainCategoryIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach> " +
            "AND is_deleted = 0 " +
            "GROUP BY main_category_id" +
            "</script>")
    List<Map<String, Object>> countByMainCategoryIds(@Param("mainCategoryIds") List<Long> mainCategoryIds);

}
