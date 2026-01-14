package com.dzy.river.chart.luo.writ.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 数据内容表 DTO
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "ContentDTO", description = "数据内容表 DTO")
public class ContentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "内容ID", example = "1")
    private Long id;

    @Schema(description = "所属小分类ID")
    private Long subCategoryId;

    @Schema(description = "内容标题")
    private String title;

    @Schema(description = "内容类型(image:图片, note:笔记)")
    private String contentType;

    @Schema(description = "图片URL(当content_type为image时使用)")
    private String imageUrl;

    @Schema(description = "图片URL列表(当content_type为image时使用)")
    private List<String> imageUrlList;

    @Schema(description = "图片缩略图URL")
    private String imageThumbnailUrl;

    @Schema(description = "图片文件大小(字节)")
    private Long imageSize;

    @Schema(description = "图片宽度")
    private Integer imageWidth;

    @Schema(description = "图片高度")
    private Integer imageHeight;

    @Schema(description = "笔记内容(当content_type为note时使用)")
    private String noteContent;

    @Schema(description = "笔记格式类型")
    private String noteFormat;

    @Schema(description = "内容描述")
    private String description;

    @Schema(description = "标签id列表, 用于创建页、编辑页的标签传输; null表示不修改, []表示情况标签")
    private List<Long> tagIdList;

    @Schema(description = "标签列表, 用于查询返回标签列表")
    private List<TagDTO> tagDTOList;

    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}