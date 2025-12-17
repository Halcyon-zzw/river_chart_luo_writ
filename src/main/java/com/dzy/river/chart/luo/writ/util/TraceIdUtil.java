package com.dzy.river.chart.luo.writ.util;

import org.slf4j.MDC;

/**
 * TraceId 工具类
 * 用于在代码中获取当前请求的 trace-id
 *
 * @author zhuzhiwei
 * @since 2025-12-17
 */
public class TraceIdUtil {

    private static final String TRACE_ID = "traceId";

    /**
     * 获取当前请求的 trace-id
     *
     * @return trace-id，如果不存在则返回空字符串
     */
    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID);
        return traceId != null ? traceId : "";
    }

    /**
     * 设置 trace-id
     * 注意：一般情况下不需要手动设置，过滤器会自动处理
     *
     * @param traceId trace-id
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * 清除 trace-id
     */
    public static void clear() {
        MDC.remove(TRACE_ID);
    }
}
