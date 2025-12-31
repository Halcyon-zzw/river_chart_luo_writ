package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dzy.river.chart.luo.writ.config.VerificationCodeProperties;
import com.dzy.river.chart.luo.writ.domain.entity.VerificationCode;
import com.dzy.river.chart.luo.writ.domain.mapper.VerificationCodeMapper;
import com.dzy.river.chart.luo.writ.exception.BusinessException;
import com.dzy.river.chart.luo.writ.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 验证码服务实现类
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Slf4j
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeMapper verificationCodeMapper;

    @Autowired
    private VerificationCodeProperties codeProperties;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    private static final Random RANDOM = new Random();

    @Override
    public boolean sendSmsCode(String phone, String purpose, String ip) {
        // 检查是否可以发送
        if (!canSendCode(phone, VerificationCode.Type.PHONE_SMS)) {
            throw new BusinessException(4001, "发送验证码过于频繁，请稍后再试");
        }

        // 生成验证码
        String code = generateCode();

        // 保存验证码记录
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setTarget(phone);
        verificationCode.setCode(code);
        verificationCode.setType(VerificationCode.Type.PHONE_SMS);
        verificationCode.setPurpose(purpose);
        verificationCode.setIpAddress(ip);
        verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(codeProperties.getExpireMinutes()));
        verificationCode.setVerified((byte) 0);
        verificationCode.setCreateTime(LocalDateTime.now());

        int inserted = verificationCodeMapper.insert(verificationCode);

        if (inserted > 0) {
            // TODO: 调用短信服务商发送短信（当前为预留接口）
            log.info("短信验证码发送成功（模拟）：手机号={}, 验证码={}, 用途={}", phone, code, purpose);
            return true;
        }

        return false;
    }

    @Override
    public boolean sendEmailCode(String email, String purpose, String ip) {
        // 检查是否可以发送
        if (!canSendCode(email, VerificationCode.Type.EMAIL)) {
            throw new BusinessException(4001, "发送验证码过于频繁，请稍后再试");
        }

        // 生成验证码
        String code = generateCode();

        // 保存验证码记录
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setTarget(email);
        verificationCode.setCode(code);
        verificationCode.setType(VerificationCode.Type.EMAIL);
        verificationCode.setPurpose(purpose);
        verificationCode.setIpAddress(ip);
        verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(codeProperties.getExpireMinutes()));
        verificationCode.setVerified((byte) 0);
        verificationCode.setCreateTime(LocalDateTime.now());

        int inserted = verificationCodeMapper.insert(verificationCode);

        if (inserted > 0) {
            // 发送邮件
            try {
                sendEmail(email, code, purpose);
                log.info("邮件验证码发送成功：邮箱={}, 验证码={}, 用途={}", email, code, purpose);
                return true;
            } catch (Exception e) {
                log.error("邮件发送失败：邮箱={}, 错误={}", email, e.getMessage());
                throw new BusinessException(5001, "邮件发送失败，请稍后重试");
            }
        }

        return false;
    }

    @Override
    public boolean verifyCode(String target, String code, String type, String purpose) {
        // 查询最新的未验证的验证码
        LambdaQueryWrapper<VerificationCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VerificationCode::getTarget, target)
                .eq(VerificationCode::getType, type)
                .eq(VerificationCode::getPurpose, purpose)
                .eq(VerificationCode::getVerified, 0)
                .orderByDesc(VerificationCode::getCreateTime)
                .last("LIMIT 1");

        VerificationCode verificationCode = verificationCodeMapper.selectOne(queryWrapper);

        if (verificationCode == null) {
            log.warn("验证码不存在：target={}, type={}, purpose={}", target, type, purpose);
            return false;
        }

        // 检查是否过期
        if (LocalDateTime.now().isAfter(verificationCode.getExpireTime())) {
            log.warn("验证码已过期：target={}, code={}, expireTime={}",
                    target, code, verificationCode.getExpireTime());
            return false;
        }

        // 验证验证码
        if (!code.equals(verificationCode.getCode())) {
            log.warn("验证码不正确：target={}, inputCode={}, correctCode={}",
                    target, code, verificationCode.getCode());
            return false;
        }

        // 标记为已验证
        verificationCode.setVerified((byte) 1);
        verificationCode.setVerifyTime(LocalDateTime.now());
        verificationCodeMapper.updateById(verificationCode);

        log.info("验证码验证成功：target={}, type={}, purpose={}", target, type, purpose);
        return true;
    }

    @Override
    public boolean canSendCode(String target, String type) {
        LocalDateTime now = LocalDateTime.now();

        // 检查最后一次发送时间（发送间隔限制）
        LambdaQueryWrapper<VerificationCode> lastQueryWrapper = new LambdaQueryWrapper<>();
        lastQueryWrapper.eq(VerificationCode::getTarget, target)
                .eq(VerificationCode::getType, type)
                .orderByDesc(VerificationCode::getCreateTime)
                .last("LIMIT 1");

        VerificationCode lastCode = verificationCodeMapper.selectOne(lastQueryWrapper);
        if (lastCode != null) {
            LocalDateTime lastSendTime = lastCode.getCreateTime();
            LocalDateTime nextAllowedTime = lastSendTime.plusSeconds(codeProperties.getSendIntervalSeconds());
            if (now.isBefore(nextAllowedTime)) {
                log.warn("发送间隔不足：target={}, 需等待到={}", target, nextAllowedTime);
                return false;
            }
        }

        // 检查在限定时间内的发送次数（最大发送次数限制）
        LocalDateTime startTime = now.minusMinutes(codeProperties.getExpireMinutes());
        Integer count = verificationCodeMapper.countByTargetAndTypeAndCreateTimeAfter(target, type, startTime);

        if (count >= codeProperties.getMaxSendTimes()) {
            log.warn("发送次数超限：target={}, count={}, maxTimes={}",
                    target, count, codeProperties.getMaxSendTimes());
            return false;
        }

        return true;
    }

    /**
     * 生成随机验证码
     *
     * @return 验证码
     */
    private String generateCode() {
        int codeLength = codeProperties.getCodeLength();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 发送邮件
     *
     * @param email   邮箱
     * @param code    验证码
     * @param purpose 用途
     */
    private void sendEmail(String email, String code, String purpose) {
        if (mailSender == null) {
            log.warn("邮件发送器未配置，跳过发送邮件");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(getPurposeText(purpose) + " - 验证码");
        message.setText("您的验证码是：" + code + "\n\n"
                + "验证码有效期为 " + codeProperties.getExpireMinutes() + " 分钟，请勿泄露给他人。\n\n"
                + "如非本人操作，请忽略此邮件。");

        mailSender.send(message);
    }

    /**
     * 获取用途文本
     *
     * @param purpose 用途代码
     * @return 用途文本
     */
    private String getPurposeText(String purpose) {
        return switch (purpose) {
            case VerificationCode.Purpose.REGISTER -> "注册账号";
            case VerificationCode.Purpose.LOGIN -> "登录账号";
            case VerificationCode.Purpose.RESET_PASSWORD -> "重置密码";
            case VerificationCode.Purpose.BIND -> "绑定账号";
            default -> "验证操作";
        };
    }
}
