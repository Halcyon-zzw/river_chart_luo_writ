package com.dzy.river.chart.luo.writ.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author zhuzhiwei
 * @since 2024-10-28
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 客户端错误
     */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "资源冲突"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    VALIDATION_ERROR(422, "数据校验失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    /**
     * 服务器错误
     */
    INTERNAL_ERROR(500, "系统内部错误"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    /**
     * 业务错误 (6000-6999)
     */
    BUSINESS_ERROR(6000, "业务处理失败"),
    USER_NOT_FOUND(6001, "用户不存在"),
    USER_ALREADY_EXISTS(6002, "用户已存在"),
    PASSWORD_ERROR(6003, "密码错误"),
    ACCOUNT_DISABLED(6004, "账户已禁用"),
    ACCOUNT_LOCKED(6005, "账户已锁定"),

    /**
     * 数据库错误 (7000-7999)
     */
    DATABASE_ERROR(7000, "数据库操作失败"),
    DATA_NOT_FOUND(7001, "数据不存在"),
    DATA_ALREADY_EXISTS(7002, "数据已存在"),
    DATA_INTEGRITY_VIOLATION(7003, "数据完整性约束违反"),

    /**
     * 外部服务错误 (8000-8999)
     */
    EXTERNAL_SERVICE_ERROR(8000, "外部服务调用失败"),
    NETWORK_ERROR(8001, "网络连接异常"),
    TIMEOUT_ERROR(8002, "调用超时");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;
}