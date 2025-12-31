package com.dzy.river.chart.luo.writ.service;

/**
 * 验证码服务接口
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
public interface VerificationCodeService {

    /**
     * 发送短信验证码
     *
     * @param phone   手机号
     * @param purpose 用途
     * @param ip      请求IP
     * @return 是否发送成功
     */
    boolean sendSmsCode(String phone, String purpose, String ip);

    /**
     * 发送邮件验证码
     *
     * @param email   邮箱
     * @param purpose 用途
     * @param ip      请求IP
     * @return 是否发送成功
     */
    boolean sendEmailCode(String email, String purpose, String ip);

    /**
     * 验证验证码
     *
     * @param target  目标（手机号或邮箱）
     * @param code    验证码
     * @param type    类型
     * @param purpose 用途
     * @return 是否验证成功
     */
    boolean verifyCode(String target, String code, String type, String purpose);

    /**
     * 检查是否可以发送验证码（频率限制）
     *
     * @param target 目标（手机号或邮箱）
     * @param type   类型
     * @return 是否可以发送
     */
    boolean canSendCode(String target, String type);
}
