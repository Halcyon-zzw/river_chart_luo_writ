-- 创建数据库
CREATE DATABASE IF NOT EXISTS `river_chart_luo_writ` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `river_chart_luo_writ`;

-- ====================================
-- 1. 用户表 (user)
-- ====================================
CREATE TABLE IF NOT EXISTS `user` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
                        `username` VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名，唯一',
                        `email` VARCHAR(128) UNIQUE COMMENT '邮箱，唯一',
                        `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                        `password_hash` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
                        `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
                        `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',

    -- 微信相关字段（小程序常用）
                        `openid` VARCHAR(128) UNIQUE COMMENT '微信openid，唯一',
                        `unionid` VARCHAR(128) UNIQUE COMMENT '微信unionid，唯一',
                        `wechat_info` JSON DEFAULT NULL COMMENT '微信用户信息，JSON格式',

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
                        UNIQUE KEY `uk_openid` (`openid`),
                        UNIQUE KEY `uk_unionid` (`unionid`),
                        KEY `idx_phone` (`phone`),
                        KEY `idx_created_time` (`created_time`),
                        KEY `idx_status_deleted` (`status`, `is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ====================================
-- 2. 标签表 (tags)
-- ====================================
CREATE TABLE IF NOT EXISTS tags (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
                      name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
                      color VARCHAR(7) DEFAULT '#007AFF' COMMENT '标签颜色(十六进制)',
                      is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                      created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ====================================
-- 3. 主分类表 (main_categories)
-- ====================================
CREATE TABLE IF NOT EXISTS main_categories (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主分类ID',
                                 name VARCHAR(100) NOT NULL COMMENT '分类名称',
                                 description TEXT COMMENT '分类描述',
                                 thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
                                 sort_order INT DEFAULT 0 COMMENT '排序权重',
                                 is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                                 created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                 INDEX idx_sort_deleted_time (sort_order, is_deleted, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主分类表';

-- ====================================
-- 4. 小分类表 (sub_categories)
-- ====================================
CREATE TABLE IF NOT EXISTS sub_categories (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '小分类ID',
                                main_category_id INT NOT NULL COMMENT '所属主分类ID',
                                name VARCHAR(100) NOT NULL COMMENT '小分类名称',
                                description TEXT COMMENT '小分类描述',
                                thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
                                sort_order INT DEFAULT 0 COMMENT '排序权重',
                                is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                                created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                INDEX idx_main_category_id (main_category_id),
                                INDEX idx_main_category_deleted (main_category_id, is_deleted, sort_order),
                                INDEX idx_sort_deleted_time (sort_order, is_deleted, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小分类表';

-- ====================================
-- 5. 数据内容表 (contents)
-- ====================================
CREATE TABLE IF NOT EXISTS contents (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '内容ID',
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
                          INDEX idx_sort_order (sort_order),
                          INDEX idx_created_time (created_time),
                          INDEX idx_type_deleted_time (content_type, is_deleted, created_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据内容表';

-- ====================================
-- 6. 主分类标签关联表 (main_category_tags)
-- ====================================
CREATE TABLE IF NOT EXISTS main_category_tags (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
                                    main_category_id INT NOT NULL COMMENT '主分类ID',
                                    tag_id INT NOT NULL COMMENT '标签ID',
                                    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                                    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                    UNIQUE KEY uk_main_category_tag (main_category_id, tag_id),
                                    INDEX idx_main_category_id (main_category_id),
                                    INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主分类标签关联表';

-- ====================================
-- 7. 小分类标签关联表 (sub_category_tags)
-- ====================================
CREATE TABLE IF NOT EXISTS sub_category_tags (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
                                   sub_category_id INT NOT NULL COMMENT '小分类ID',
                                   tag_id INT NOT NULL COMMENT '标签ID',
                                   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                                   created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                   UNIQUE KEY uk_sub_category_tag (sub_category_id, tag_id),
                                   INDEX idx_sub_category_id (sub_category_id),
                                   INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小分类标签关联表';

-- ====================================
-- 8. 内容标签关联表 (content_tags)
-- ====================================
CREATE TABLE IF NOT EXISTS content_tags (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
                              content_id INT NOT NULL COMMENT '内容ID',
                              tag_id INT NOT NULL COMMENT '标签ID',
                              is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                              created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                              UNIQUE KEY uk_content_tag (content_id, tag_id),
                              INDEX idx_content_id (content_id),
                              INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容标签关联表';

-- ====================================
-- 9. 用户收藏表 (user_favorites)
-- ====================================
CREATE TABLE IF NOT EXISTS user_favorites (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
                                user_id BIGINT NOT NULL COMMENT '用户ID（逻辑关联user.id）',
                                content_id INT NOT NULL COMMENT '收藏的内容ID（逻辑关联contents.id）',
                                folder_name VARCHAR(100) DEFAULT '默认收藏夹' COMMENT '收藏夹名称',
                                tags JSON COMMENT '收藏标签，JSON数组格式',
                                notes TEXT COMMENT '收藏备注',
                                is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                                created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                UNIQUE KEY uk_user_content (user_id, content_id),
                                INDEX idx_user_id (user_id),
                                INDEX idx_content_id (content_id),
                                INDEX idx_created_time (created_time),
                                INDEX idx_user_deleted_time (user_id, is_deleted, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';


-- ====================================
-- 10. 操作日志表 (operation_logs)
-- ====================================
CREATE TABLE IF NOT EXISTS operation_logs (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
                                user_id BIGINT NOT NULL COMMENT '操作用户ID（逻辑关联user.id）',
                                operation_type VARCHAR(50) NOT NULL COMMENT '操作类型：CREATE, UPDATE, DELETE, VIEW, LOGIN, etc.',
                                target_table VARCHAR(50) COMMENT '操作目标表',
                                target_id VARCHAR(100) COMMENT '操作目标ID',
                                operation_description VARCHAR(500) COMMENT '操作描述',
                                old_values JSON COMMENT '旧值（JSON格式）',
                                new_values JSON COMMENT '新值（JSON格式）',
                                ip_address VARCHAR(64) COMMENT '操作IP地址',
                                user_agent VARCHAR(500) COMMENT '用户代理',
                                is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                                created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

                                INDEX idx_user_id (user_id),
                                INDEX idx_operation_type (operation_type),
                                INDEX idx_target_table (target_table),
                                INDEX idx_created_time (created_time),
                                INDEX idx_user_operation_time (user_id, operation_type, created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';


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