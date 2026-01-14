package com.dzy.river.chart.luo.writ.mapper;

import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据内容表 Mapper 接口
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Mapper
public interface ContentMapper extends BaseMapper<Content> {

    /**
     * 批量统计子分类对应的内容数量
     *
     * @param subCategoryIds 子分类ID列表
     * @return 统计结果，key: sub_category_id, value: count
     */
    @Select("<script>" +
            "SELECT sub_category_id, COUNT(*) as count " +
            "FROM content " +
            "WHERE sub_category_id IN " +
            "<foreach collection='subCategoryIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach> " +
            "AND is_deleted = 0 " +
            "GROUP BY sub_category_id" +
            "</script>")
    List<Map<String, Object>> countBySubCategoryIds(@Param("subCategoryIds") List<Long> subCategoryIds);

    /**
     * 根据标题和内容类型查询内容ID列表
     *
     * @param title 标题关键词（模糊查询）
     * @param contentType 内容类型
     * @return 内容ID列表
     */
    @Select("<script>" +
            "SELECT id FROM content WHERE is_deleted = 0 " +
            "<if test='title != null and title != \"\"'>" +
            "AND title LIKE CONCAT('%', #{title}, '%') " +
            "</if>" +
            "<if test='contentType != null and contentType != \"\"'>" +
            "AND content_type = #{contentType} " +
            "</if>" +
            "</script>")
    List<Long> selectIdsByTitleAndType(@Param("title") String title, @Param("contentType") String contentType);

}
