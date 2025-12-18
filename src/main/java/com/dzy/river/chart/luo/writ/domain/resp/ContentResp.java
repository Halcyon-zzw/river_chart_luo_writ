package com.dzy.river.chart.luo.writ.domain.resp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 数据内容表 Resp
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "ContentResp", description = "数据内容表 Resp")
public class ContentResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "内容ID", example = "1")
    private Long id;

    @Schema(description = "所属小分类ID")
    private Integer subCategoryId;

    @Schema(description = "内容标题")
    private String title;

    @Schema(description = "内容类型(image:图片, note:笔记)")
    private String contentType;

    @Schema(description = "图片URL(当content_type为image时使用)")
    private String imageUrl;

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

    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "查看次数")
    private Integer viewCount;

    @Schema(description = "标签列表")
    private List<TagDTO> tagDTOList;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

}