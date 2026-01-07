package com.dzy.river.chart.luo.writ.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplate配置类
 * 配置HTTP客户端的连接超时和读取超时
 *
 * @author zhuzhiwei
 * @date 2026/01/07
 */
@Slf4j
@Configuration
public class RestTemplateConfig {

    @Autowired
    private HttpTimeoutProperties httpTimeoutProperties;

    /**
     * 创建配置了超时时间的RestTemplate Bean
     *
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        log.info("Initializing RestTemplate with connect timeout: {}ms, read timeout: {}ms",
                httpTimeoutProperties.getConnect(), httpTimeoutProperties.getRead());

        return builder
                .setConnectTimeout(Duration.ofMillis(httpTimeoutProperties.getConnect()))
                .setReadTimeout(Duration.ofMillis(httpTimeoutProperties.getRead()))
                .build();
    }

    /**
     * 创建ClientHttpRequestFactory（备用方法）
     * 如果需要更细粒度的控制，可以使用这个方法
     *
     * @return ClientHttpRequestFactory实例
     */
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(httpTimeoutProperties.getConnect());
        factory.setReadTimeout(httpTimeoutProperties.getRead());
        return factory;
    }
}
