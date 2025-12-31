package com.dzy.river.chart.luo.writ.context;

/**
 * 用户上下文 - 基于ThreadLocal存储当前请求的用户ID
 * 用于在整个请求链路中传递用户信息
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
public class UserContext {

    /**
     * ThreadLocal存储用户ID
     * 每个线程独立存储，保证线程安全
     */
    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    /**
     * 默认用户ID（未登录或token验证失败时使用）
     */
    private static final Long DEFAULT_USER_ID = -1L;

    /**
     * 设置当前线程的用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取当前线程的用户ID
     *
     * @return 用户ID，如果未设置则返回-1
     */
    public static Long getUserId() {
        Long userId = USER_ID_HOLDER.get();
        return userId != null ? userId : DEFAULT_USER_ID;
    }

    /**
     * 清除当前线程的用户ID
     * 必须在请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
    }

    /**
     * 私有构造函数，防止实例化
     */
    private UserContext() {
        throw new UnsupportedOperationException("Context class cannot be instantiated");
    }
}
