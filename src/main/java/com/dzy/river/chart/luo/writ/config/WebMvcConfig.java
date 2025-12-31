package com.dzy.river.chart.luo.writ.config;

import com.dzy.river.chart.luo.writ.interceptor.JwtAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 *
 * @author zhuzhiwei
 * @date 2025/12/18
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射，使上传的文件可以通过 URL 访问
        registry.addResourceHandler(urlPrefix + "**")
                .addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册JWT认证拦截器
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .excludePathPatterns(
                        "/auth/**",              // 排除认证相关接口
                        "/verification-code/**", // 排除验证码接口
                        "/swagger-ui/**",        // 排除Swagger UI
                        "/swagger-ui.html",      // 排除Swagger UI首页
                        "/v3/api-docs/**",       // 排除OpenAPI文档
                        "/swagger-resources/**", // 排除Swagger资源
                        "/webjars/**"            // 排除Swagger静态资源
                );
    }
}
