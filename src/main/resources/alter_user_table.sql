-- ====================================
-- 优化User表结构以支持多种登录方式
-- ====================================

USE `river_chart_luo_writ`;

-- 1. 添加手机号验证标志字段
ALTER TABLE `user` ADD COLUMN `phone_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '手机号验证标志：0-未验证，1-已验证' AFTER `phone`;

-- 2. 添加邮箱验证标志字段
ALTER TABLE `user` ADD COLUMN `email_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '邮箱验证标志：0-未验证，1-已验证' AFTER `email`;

-- 3. 修改password_hash字段为可空（支持微信登录、验证码登录等无密码场景）
ALTER TABLE `user` MODIFY COLUMN `password_hash` VARCHAR(255) DEFAULT NULL COMMENT '加密后的密码（可为空，支持无密码登录）';

-- 4. 为phone字段添加唯一约束（如果已存在重复数据，需要先清理）
-- 注意：执行前请确保phone字段没有重复值，且NULL值不会违反唯一约束
ALTER TABLE `user` ADD UNIQUE KEY `uk_phone` (`phone`);

-- 5. 删除原有的phone普通索引（已被唯一索引替代）
ALTER TABLE `user` DROP INDEX `idx_phone`;

-- ====================================
-- 说明：
-- 1. phone_verified: 用于判断用户是否通过短信验证码验证过手机号
-- 2. email_verified: 用于判断用户是否通过邮件验证码验证过邮箱
-- 3. password_hash允许为空: 支持微信登录、验证码登录等场景
-- 4. phone唯一约束: 确保一个手机号只能注册一个账号
-- 5. email已有唯一约束，无需修改
-- ====================================
