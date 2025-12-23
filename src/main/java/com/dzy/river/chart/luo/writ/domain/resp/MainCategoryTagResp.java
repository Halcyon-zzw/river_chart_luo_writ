package com.dzy.river.chart.luo.writ.domain.resp;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 主分类标签关联表 Resp
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "MainCategoryTagResp", description = "主分类标签关联表 Resp")
public class MainCategoryTagResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "关联ID", example = "1")
    private Long id;

    @Schema(description = "主分类ID")
    private Long mainCategoryId;

    @Schema(description = "标签ID")
    private Long tagId;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}