package com.dzy.river.chart.luo.writ.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 *
 * @author zhuzhiwei
 * @date 2025/10/27 17:10
 */
@Configuration
@MapperScan("com.dzy.river.chart.luo.writ.mapper")
public class MybatisPlusConfig {

    /**
     * 配置MyBatis Plus拦截器
     * 1. 分页插件：自动执行count查询和分页查询
     * 2. 乐观锁插件：支持@Version注解的乐观锁
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1. 添加分页插件（必须在最前面）
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置请求的页面大于最大页后的操作，true返回首页，false继续请求（默认false）
        paginationInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        paginationInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInterceptor);

        // 2. 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }
}
