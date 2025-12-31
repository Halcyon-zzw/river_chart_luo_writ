package com.dzy.river.chart.luo.writ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置密码加密器
     * 使用BCrypt算法
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置Security过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（使用JWT不需要CSRF保护）
                .csrf(AbstractHttpConfigurer::disable)
                // 配置Session管理为无状态（使用JWT不需要Session）
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        // 允许认证相关端点无需认证
                        .requestMatchers(
                                "/auth/**",           // 认证相关接口
                                "/verification-code/**" // 验证码接口
                        ).permitAll()
                        // Swagger UI和API文档端点
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
