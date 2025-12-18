package com.dzy.river.chart.luo.writ.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据内容表
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("content")
@Schema(name = "Content", description = "数据内容表")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内容ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属小分类ID
     */
    @TableField("sub_category_id")
    private Long subCategoryId;

    /**
     * 内容标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容类型(image:图片, note:笔记)
     */
    @TableField("content_type")
    private String contentType;

    /**
     * 图片URL(当content_type为image时使用)
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 图片缩略图URL
     */
    @TableField("image_thumbnail_url")
    private String imageThumbnailUrl;

    /**
     * 图片文件大小(字节)
     */
    @TableField("image_size")
    private Long imageSize;

    /**
     * 图片宽度
     */
    @TableField("image_width")
    private Integer imageWidth;

    /**
     * 图片高度
     */
    @TableField("image_height")
    private Integer imageHeight;

    /**
     * 笔记内容(当content_type为note时使用)
     */
    @TableField("note_content")
    private String noteContent;

    /**
     * 笔记格式类型
     */
    @TableField("note_format")
    private String noteFormat;

    /**
     * 内容描述
     */
    @TableField("description")
    private String description;

    /**
     * 排序权重
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}