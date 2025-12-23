package com.dzy.river.chart.luo.writ.domain.resp;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 主分类表 Resp
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "MainCategoryResp", description = "主分类表 Resp")
public class MainCategoryResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主分类ID", example = "1")
    private Long id;

    @Schema(description = "分类名称")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}