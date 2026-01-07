package com.dzy.river.chart.luo.writ.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * HTTP客户端超时配置属性
 *
 * @author zhuzhiwei
 * @date 2026/01/07
 */
@Data
@Component
@ConfigurationProperties(prefix = "myapp.http.timeout")
public class HttpTimeoutProperties {

    /**
     * 连接超时时间（毫秒），默认10秒
     */
    private int connect = 10000;

    /**
     * 读取超时时间（毫秒），默认10秒
     */
    private int read = 10000;
}
