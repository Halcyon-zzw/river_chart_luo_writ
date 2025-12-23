package com.dzy.river.chart.luo.writ.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 用户收藏表 DTO
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserCollectionDTO", description = "用户收藏表 DTO")
public class UserCollectionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "收藏ID", example = "1")
    private Long id;

    @Schema(description = "用户ID（逻辑关联user.id）")
    private Long userId;

    @Schema(description = "收藏的内容ID（逻辑关联contents.id）")
    private Long contentId;

    @Schema(description = "收藏夹名称")
    private String folderName;

    @Schema(description = "收藏标签，JSON数组格式")
    private String tags;

    @Schema(description = "收藏备注")
    private String notes;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}