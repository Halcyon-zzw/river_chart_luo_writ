-- 浏览历史表
CREATE TABLE browse_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    content_id BIGINT NOT NULL COMMENT '内容ID',
    user_id BIGINT COMMENT '用户ID（可为空，支持匿名浏览）',
    browse_count INT NOT NULL DEFAULT 1 COMMENT '浏览次数',
    last_browse_time DATETIME NOT NULL COMMENT '最后浏览时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    UNIQUE KEY uk_content_user_deleted (content_id, user_id, is_deleted) COMMENT '内容用户删除标志唯一索引',
    INDEX idx_content_id (content_id) COMMENT '内容ID索引',
    INDEX idx_user_id (user_id) COMMENT '用户ID索引',
    INDEX idx_last_browse_time (last_browse_time) COMMENT '最后浏览时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览历史表';
