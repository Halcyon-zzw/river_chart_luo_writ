package com.dzy.river.chart.luo.writ.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 验证码配置属性类
 * 从Nacos配置中心读取验证码相关配置
 * 支持动态刷新
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "verification-code")
public class VerificationCodeProperties {

    /**
     * 验证码有效期（分钟）
     * 默认5分钟
     */
    private Integer expireMinutes = 5;

    /**
     * 发送间隔（秒）
     * 默认60秒，即1分钟内只能发送一次
     */
    private Integer sendIntervalSeconds = 60;

    /**
     * 最多发送次数
     * 默认在5分钟内最多发送3次
     */
    private Integer maxSendTimes = 3;

    /**
     * 验证码长度
     * 默认6位数字
     */
    private Integer codeLength = 6;
}
