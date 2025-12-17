package com.dzy.river.chart.luo.writ.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * 链路追踪过滤器
 * 为每个请求生成唯一的 trace-id，并存入 MDC 中
 *
 * @author zhuzhiwei
 * @since 2025-12-17
 */
@Component
@Order(1)
public class TraceFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            // 尝试从请求头获取 trace-id，如果没有则生成新的
            String traceId = httpRequest.getHeader(TRACE_ID_HEADER);
            if (traceId == null || traceId.isEmpty()) {
                traceId = generateTraceId();
            }

            // 将 trace-id 存入 MDC
            MDC.put(TRACE_ID, traceId);

            // 将 trace-id 添加到响应头中
            httpResponse.setHeader(TRACE_ID_HEADER, traceId);

            // 继续执行后续过滤器和处理器
            chain.doFilter(request, response);
        } finally {
            // 请求结束后清理 MDC，避免内存泄漏
            MDC.clear();
        }
    }

    /**
     * 生成唯一的 trace-id
     * 使用 UUID 去掉横线，取前16位
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
