-- 说明书小程序数据库表结构设计
-- 创建时间: 2025-10-28

CREATE DATABASE IF NOT EXISTS `river_chart_luo_writ`

USE `river_chart_luo_writ`;

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
    `username` VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名，唯一',
    `email` VARCHAR(128) UNIQUE COMMENT '邮箱，唯一',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',

    -- 状态相关字段
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用，2-锁定',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',

    -- 权限相关
    `role` VARCHAR(32) NOT NULL DEFAULT 'USER' COMMENT '角色：SUPER_ADMIN-超级管理员, ADMIN-管理员, USER-普通用户',
    `permissions` JSON DEFAULT NULL COMMENT '额外权限配置，JSON格式',

    -- 时间戳
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
    `password_update_time` DATETIME DEFAULT NULL COMMENT '密码最后修改时间',
    `account_expire_time` DATETIME DEFAULT NULL COMMENT '账号过期时间',

    -- 审计字段
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB COMMENT='用户表';

-- ====================================
-- 1. 标签表 (tags)
-- ====================================
CREATE TABLE tags (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
    color VARCHAR(7) DEFAULT '#007AFF' COMMENT '标签颜色(十六进制)',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ====================================
-- 2. 主分类表 (main_categories)
-- ====================================
CREATE TABLE main_categories (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_sort_order (sort_order),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主分类表';

-- ====================================
-- 3. 小分类表 (sub_categories)
-- ====================================
CREATE TABLE sub_categories (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '小分类ID',
    main_category_id INT NOT NULL COMMENT '所属主分类ID',
    name VARCHAR(100) NOT NULL COMMENT '小分类名称',
    description TEXT COMMENT '小分类描述',
    thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_main_category_id (main_category_id),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小分类表';

-- ====================================
-- 4. 数据内容表 (contents)
-- ====================================
CREATE TABLE contents (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '内容ID',
    sub_category_id INT NOT NULL COMMENT '所属小分类ID',
    title VARCHAR(200) NOT NULL COMMENT '内容标题',
    content_type ENUM('image', 'note') NOT NULL COMMENT '内容类型(image:图片, note:笔记)',

    -- 图片相关字段
    image_url VARCHAR(500) COMMENT '图片URL(当content_type为image时使用)',
    image_thumbnail_url VARCHAR(500) COMMENT '图片缩略图URL',
    image_size BIGINT COMMENT '图片文件大小(字节)',
    image_width INT COMMENT '图片宽度',
    image_height INT COMMENT '图片高度',

    -- 笔记相关字段
    note_content LONGTEXT COMMENT '笔记内容(当content_type为note时使用)',
    note_format ENUM('text', 'markdown', 'html') DEFAULT 'text' COMMENT '笔记格式类型',

    -- 通用字段
    description TEXT COMMENT '内容描述',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    view_count INT DEFAULT 0 COMMENT '查看次数',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_sub_category_id (sub_category_id),
    INDEX idx_content_type (content_type),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_created_time (created_time),
    FULLTEXT KEY ft_title_description (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据内容表';

-- ====================================
-- 5. 主分类标签关联表 (main_category_tags)
-- ====================================
CREATE TABLE main_category_tags (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    main_category_id INT NOT NULL COMMENT '主分类ID',
    tag_id INT NOT NULL COMMENT '标签ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    UNIQUE KEY uk_main_category_tag (main_category_id, tag_id),
    INDEX idx_main_category_id (main_category_id),
    INDEX idx_tag_id (tag_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主分类标签关联表';

-- ====================================
-- 6. 小分类标签关联表 (sub_category_tags)
-- ====================================
CREATE TABLE sub_category_tags (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    sub_category_id INT NOT NULL COMMENT '小分类ID',
    tag_id INT NOT NULL COMMENT '标签ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    UNIQUE KEY uk_sub_category_tag (sub_category_id, tag_id),
    INDEX idx_sub_category_id (sub_category_id),
    INDEX idx_tag_id (tag_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小分类标签关联表';

-- ====================================
-- 7. 内容标签关联表 (content_tags)
-- ====================================
CREATE TABLE content_tags (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    content_id INT NOT NULL COMMENT '内容ID',
    tag_id INT NOT NULL COMMENT '标签ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    UNIQUE KEY uk_content_tag (content_id, tag_id),
    INDEX idx_content_id (content_id),
    INDEX idx_tag_id (tag_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容标签关联表';

-- ====================================
-- 8. 用户收藏表 (user_favorites) - 可选扩展
-- ====================================
CREATE TABLE user_favorites (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id VARCHAR(50) NOT NULL COMMENT '用户ID(微信openid等)',
    content_id INT NOT NULL COMMENT '收藏的内容ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    UNIQUE KEY uk_user_content (user_id, content_id),
    INDEX idx_user_id (user_id),
    INDEX idx_content_id (content_id),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- ====================================
-- 9. 系统配置表 (system_configs) - 可选扩展
-- ====================================
CREATE TABLE system_configs (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_description VARCHAR(255) COMMENT '配置描述',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_config_key (config_key),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ====================================
-- 初始化数据
-- ====================================

-- 插入优化后的基础标签
INSERT INTO tags (name, color) VALUES
('重要', '#FF3B30'),
('急用', '#FF6B35'),
('常用', '#FF9500'),
('推荐', '#FFD60A'),
('收藏', '#32D74B'),
('维修', '#007AFF'),
('安装', '#5E5CE6'),
('说明', '#AF52DE'),
('保修', '#FF2D92'),
('配件', '#A2845E'),
('工具', '#8E8E93'),
('电器', '#48CAE4'),
('数码', '#6A4C93'),
('家居', '#95D5B2'),
('办公', '#F9C74F');

-- 插入示例主分类
INSERT INTO main_categories (name, description, sort_order) VALUES
('家用电器说明书', '包含各种家用电器的使用说明书和维修手册，如冰箱、洗衣机、空调等', 1),
('数码电子设备', '手机、电脑、平板、相机等数码产品的说明书和使用指南', 2),
('工具设备手册', '各种工具和设备的操作指南，包括电动工具、测量仪器等', 3),
('家具装修指南', '家具安装说明、装修材料使用指南等相关文档', 4),
('车辆维修手册', '汽车、摩托车等交通工具的维修保养手册', 5);

-- 插入系统配置示例
INSERT INTO system_configs (config_key, config_value, config_description) VALUES
('max_upload_size', '10485760', '最大上传文件大小(字节) - 10MB'),
('supported_image_types', 'jpg,jpeg,png,gif,webp,bmp', '支持的图片格式'),
('thumbnail_width', '300', '缩略图宽度(像素)'),
('thumbnail_height', '300', '缩略图高度(像素)'),
('max_note_length', '65535', '笔记内容最大长度'),
('default_page_size', '20', '默认分页大小'),
('cache_expire_time', '3600', '缓存过期时间(秒)');