package com.dzy.river.chart.luo.writ.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性类
 * 从Nacos配置中心读取JWT相关配置
 * 支持动态刷新
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT签名密钥
     * 至少32个字符，生产环境必须修改
     */
    private String secret;

    /**
     * Access Token过期时间（秒）
     * 默认2小时 = 7200秒
     */
    private Long accessTokenExpiration = 7200L;

    /**
     * Refresh Token过期时间（秒）
     * 默认7天 = 604800秒
     */
    private Long refreshTokenExpiration = 604800L;

    /**
     * JWT签发者
     */
    private String issuer = "river-chart-luo-writ";
}
