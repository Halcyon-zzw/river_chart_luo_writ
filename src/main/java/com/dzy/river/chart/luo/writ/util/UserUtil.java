package com.dzy.river.chart.luo.writ.util;

import com.dzy.river.chart.luo.writ.context.UserContext;

/**
 * 用户工具类 - 提供便捷的用户信息访问方法
 * 可在项目中任何地方调用，无需传递HttpServletRequest参数
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
public class UserUtil {

    /**
     * 获取当前请求的用户ID
     * 从ThreadLocal中获取，无需传递HttpServletRequest
     *
     * @return 用户ID，如果未登录或token验证失败则返回-1
     */
    public static Long getUserId() {
        return UserContext.getUserId();
    }

    /**
     * 私有构造函数，防止实例化
     */
    private UserUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
