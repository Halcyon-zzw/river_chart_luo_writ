package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dzy.river.chart.luo.writ.config.JwtProperties;
import com.dzy.river.chart.luo.writ.domain.dto.auth.*;
import com.dzy.river.chart.luo.writ.domain.entity.User;
import com.dzy.river.chart.luo.writ.domain.entity.VerificationCode;
import com.dzy.river.chart.luo.writ.mapper.UserMapper;
import com.dzy.river.chart.luo.writ.exception.BusinessException;
import com.dzy.river.chart.luo.writ.service.AuthService;
import com.dzy.river.chart.luo.writ.service.VerificationCodeService;
import com.dzy.river.chart.luo.writ.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> usernameQuery = new LambdaQueryWrapper<>();
        usernameQuery.eq(User::getUsername, request.getUsername())
                .eq(User::getIsDeleted, 0);
        if (userMapper.selectCount(usernameQuery) > 0) {
            throw new BusinessException(4002, "用户名已存在");
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
            emailQuery.eq(User::getEmail, request.getEmail())
                    .eq(User::getIsDeleted, 0);
            if (userMapper.selectCount(emailQuery) > 0) {
                throw new BusinessException(4003, "邮箱已被注册");
            }
        }

        // 检查手机号是否已存在
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            LambdaQueryWrapper<User> phoneQuery = new LambdaQueryWrapper<>();
            phoneQuery.eq(User::getPhone, request.getPhone())
                    .eq(User::getIsDeleted, 0);
            if (userMapper.selectCount(phoneQuery) > 0) {
                throw new BusinessException(4004, "手机号已被注册");
            }
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setStatus((byte) 1); // 启用状态
        user.setRole("USER"); // 普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        log.info("用户注册成功：userId={}, username={}", user.getId(), user.getUsername());

        // 生成token并返回
        return generateAuthResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse passwordLogin(PasswordLoginRequest request, String ip) {
        // 根据账号查询用户（支持用户名/手机号/邮箱）
        User user = findUserByAccount(request.getAccount());

        if (user == null) {
            throw new BusinessException(4005, "账号或密码错误");
        }

        // 验证密码
        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(4005, "账号或密码错误");
        }

        // 检查账号状态
        checkUserStatus(user);

        // 更新最后登录信息
        updateLastLogin(user, ip);

        log.info("用户密码登录成功：userId={}, username={}, account={}", user.getId(), user.getUsername(), request.getAccount());

        // 生成token并返回
        return generateAuthResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse codeLogin(CodeLoginRequest request, String ip) {
        // 判断是手机号还是邮箱
        boolean isPhone = request.getAccount().matches("^1[3-9]\\d{9}$");
        boolean isEmail = request.getAccount().matches("^[A-Za-z0-9+_.-]+@(.+)$");

        if (!isPhone && !isEmail) {
            throw new BusinessException(4006, "账号格式不正确");
        }

        String codeType = isPhone ? VerificationCode.Type.PHONE_SMS : VerificationCode.Type.EMAIL;

        // 验证验证码
        boolean verified = verificationCodeService.verifyCode(
                request.getAccount(),
                request.getCode(),
                codeType,
                VerificationCode.Purpose.LOGIN
        );

        if (!verified) {
            throw new BusinessException(4007, "验证码错误或已过期");
        }

        // 查询用户是否存在
        User user = isPhone ? findUserByPhone(request.getAccount()) : findUserByEmail(request.getAccount());

        // 如果用户不存在，自动注册
        if (user == null) {
            user = autoRegisterByCode(request.getAccount(), isPhone);
            log.info("验证码登录自动注册新用户：userId={}, account={}", user.getId(), request.getAccount());
        } else {
            // 更新验证标志
            if (isPhone && user.getPhoneVerified() == 0) {
                user.setPhoneVerified((byte) 1);
                userMapper.updateById(user);
            } else if (isEmail && user.getEmailVerified() == 0) {
                user.setEmailVerified((byte) 1);
                userMapper.updateById(user);
            }
        }

        // 检查账号状态
        checkUserStatus(user);

        // 更新最后登录信息
        updateLastLogin(user, ip);

        log.info("用户验证码登录成功：userId={}, username={}, account={}", user.getId(), user.getUsername(), request.getAccount());

        // 生成token并返回
        return generateAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // 验证refresh token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(4008, "刷新令牌无效或已过期");
        }

        // 验证token类型
        if (!jwtUtil.validateTokenType(refreshToken, JwtUtil.TOKEN_TYPE_REFRESH)) {
            throw new BusinessException(4009, "令牌类型错误");
        }

        // 从refresh token中获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        if (userId == null || username == null) {
            throw new BusinessException(4010, "令牌信息不完整");
        }

        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null || user.getIsDeleted() == 1) {
            throw new BusinessException(4011, "用户不存在");
        }

        // 检查账号状态
        checkUserStatus(user);

        log.info("刷新令牌成功：userId={}, username={}", userId, username);

        // 生成新的access token和refresh token
        return generateAuthResponse(user);
    }

    @Override
    public boolean sendVerificationCode(SendCodeRequest request, String ip) {
        String target = request.getTarget();
        String type = request.getType();
        String purpose = request.getPurpose();

        // 验证type和purpose参数
        if (!type.equals(VerificationCode.Type.PHONE_SMS) && !type.equals(VerificationCode.Type.EMAIL)) {
            throw new BusinessException(4012, "验证码类型不正确");
        }

        // 根据类型发送验证码
        if (type.equals(VerificationCode.Type.PHONE_SMS)) {
            return verificationCodeService.sendSmsCode(target, purpose, ip);
        } else {
            return verificationCodeService.sendEmailCode(target, purpose, ip);
        }
    }

    /**
     * 根据账号查找用户（支持用户名/手机号/邮箱）
     */
    private User findUserByAccount(String account) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIsDeleted, 0)
                .and(wrapper -> wrapper
                        .eq(User::getUsername, account)
                        .or().eq(User::getPhone, account)
                        .or().eq(User::getEmail, account)
                )
                .last("LIMIT 1");

        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据手机号查找用户
     */
    private User findUserByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone)
                .eq(User::getIsDeleted, 0)
                .last("LIMIT 1");

        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据邮箱查找用户
     */
    private User findUserByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                .eq(User::getIsDeleted, 0)
                .last("LIMIT 1");

        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 通过验证码自动注册用户
     */
    private User autoRegisterByCode(String account, boolean isPhone) {
        User user = new User();

        if (isPhone) {
            // 生成默认用户名：phone_前8位
            user.setUsername("phone_" + account.substring(0, Math.min(8, account.length())));
            user.setPhone(account);
            user.setPhoneVerified((byte) 1);
        } else {
            // 生成默认用户名：email前缀
            String emailPrefix = account.substring(0, account.indexOf('@'));
            user.setUsername("email_" + emailPrefix);
            user.setEmail(account);
            user.setEmailVerified((byte) 1);
        }

        user.setNickname(user.getUsername());
        user.setStatus((byte) 1);
        user.setRole("USER");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        return user;
    }

    /**
     * 检查用户状态
     */
    private void checkUserStatus(User user) {
        if (user.getStatus() == 0) {
            throw new BusinessException(4013, "账号已被禁用");
        }
        if (user.getStatus() == 2) {
            throw new BusinessException(4014, "账号已被锁定");
        }
        if (user.getAccountExpireTime() != null && LocalDateTime.now().isAfter(user.getAccountExpireTime())) {
            throw new BusinessException(4015, "账号已过期");
        }
    }

    /**
     * 更新最后登录信息
     */
    private void updateLastLogin(User user, String ip) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ip);
        userMapper.updateById(user);
    }

    /**
     * 生成认证响应
     */
    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessTokenExpiration())
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }
}
