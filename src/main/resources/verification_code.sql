-- ====================================
-- 验证码表 (verification_code)
-- ====================================

USE `river_chart_luo_writ`;

CREATE TABLE IF NOT EXISTS `verification_code` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '验证码记录ID',
    `target` VARCHAR(128) NOT NULL COMMENT '目标（手机号或邮箱）',
    `code` VARCHAR(10) NOT NULL COMMENT '验证码',
    `type` VARCHAR(20) NOT NULL COMMENT '类型：PHONE_SMS-手机短信，EMAIL-邮件',
    `purpose` VARCHAR(50) NOT NULL COMMENT '用途：REGISTER-注册，LOGIN-登录，RESET_PASSWORD-重置密码，BIND-绑定',
    `ip_address` VARCHAR(64) DEFAULT NULL COMMENT '请求IP地址',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `verified` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已验证：0-未验证，1-已验证',
    `verify_time` DATETIME DEFAULT NULL COMMENT '验证时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    KEY `idx_target_type_verified` (`target`, `type`, `verified`, `create_time`),
    KEY `idx_expire_time` (`expire_time`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='验证码表';

-- ====================================
-- 说明：
-- 1. target: 存储手机号或邮箱地址
-- 2. type: 区分短信验证码和邮件验证码
-- 3. purpose: 记录验证码用途，便于追踪和审计
-- 4. expire_time: 过期时间，默认5分钟后过期
-- 5. verified: 验证标志，防止验证码被重复使用
-- 6. ip_address: 记录请求IP，用于安全审计和防刷
-- 7. 复合索引 idx_target_type_verified: 优化查询某个目标的未验证验证码
-- ====================================
