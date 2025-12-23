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
 * 用户收藏表
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_collection")
@Schema(name = "UserCollection", description = "用户收藏表")
public class UserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（逻辑关联user.id）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 收藏的内容ID（逻辑关联contents.id）
     */
    @TableField("content_id")
    private Long contentId;

    /**
     * 收藏夹名称
     */
    @TableField("folder_name")
    private String folderName;

    /**
     * 收藏标签，JSON数组格式
     */
    @TableField("tags")
    private String tags;

    /**
     * 收藏备注
     */
    @TableField("notes")
    private String notes;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}