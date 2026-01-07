package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.domain.entity.User;
import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import com.dzy.river.chart.luo.writ.domain.convert.UserConvert;
import com.dzy.river.chart.luo.writ.domain.req.LoginReq;
import com.dzy.river.chart.luo.writ.dao.UserDao;
import com.dzy.river.chart.luo.writ.exception.BusinessException;
import com.dzy.river.chart.luo.writ.manager.UserManager;
import com.dzy.river.chart.luo.writ.service.UserService;
import com.dzy.river.chart.luo.writ.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConvert userConvert;

    @Autowired
    private UserManager userManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDTO getById(Long id) {
        User user = userDao.getById(id);
        return user != null ? userConvert.toUserDTO(user) : null;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = userConvert.toUser(userDTO);
        boolean success = userDao.save(user);
        return success ? userConvert.toUserDTO(user) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return userDao.removeById(id);
    }

    @Override
    public UserDTO updateById(Long id, UserDTO userDTO) {
        userDTO.setId(id);
        User user = userConvert.toUser(userDTO);
        boolean success = userDao.updateById(user);
        return success ? userConvert.toUserDTO(user) : null;
    }

    @Override
    public UserDTO login(LoginReq loginReq) {
        User user;

        // 1. 判断登录方式并查询用户
        if (loginReq.getUsername() != null && !loginReq.getUsername().trim().isEmpty()) {
            // 用户名/密码登录
            user = userDao.selectByUsername(loginReq.getUsername());
            if (user == null) {
                log.warn("Login failed: user not found, username={}", loginReq.getUsername());
                throw new BusinessException("用户名或密码错误");
            }

            // 验证密码
            if (loginReq.getPassword() == null || loginReq.getPassword().trim().isEmpty()) {
                log.warn("Login failed: password is empty, username={}", loginReq.getUsername());
                throw new BusinessException("用户名或密码错误");
            }

            // TODO: 这里暂时使用简单的字符串比较，后续应该使用加密算法验证密码
            if (!loginReq.getPassword().equals(user.getPasswordHash())) {
                log.warn("Login failed: password mismatch, username={}", loginReq.getUsername());
                throw new BusinessException("用户名或密码错误");
            }

        } else if (loginReq.getOpenid() != null && !loginReq.getOpenid().trim().isEmpty()) {
            // 微信登录
            user = userDao.selectByOpenid(loginReq.getOpenid());
            if (user == null) {
                // 微信用户不存在，自动注册
                log.info("Wechat user not found, auto register. openid={}", loginReq.getOpenid());
                user = autoRegisterWechatUser(loginReq);
            }

        } else {
            // 两种登录方式都没有提供
            log.warn("Login failed: neither username nor openid provided");
            throw new BusinessException("请提供用户名密码或微信openid");
        }



        // 2. 检查用户状态
        userManager.checkUser(user);

        // 3. 生成token
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 4. 转换为DTO并设置token
        UserDTO userDTO = userConvert.toUserDTO(user);
        userDTO.setUserId(user.getId());
        userDTO.setToken(accessToken);
        userDTO.setRefreshToken(refreshToken);

        log.info("Login success: userId={}, username={}", user.getId(), user.getUsername());

        return userDTO;
    }

    /**
     * 自动注册微信用户
     *
     * @param loginReq 登录请求（包含微信用户信息）
     * @return 创建的用户
     */
    private User autoRegisterWechatUser(LoginReq loginReq) {
        User newUser = new User();

        // 1. 设置微信信息
        newUser.setOpenid(loginReq.getOpenid());
        newUser.setUnionid(loginReq.getUnionid());
        newUser.setWechatInfo(loginReq.getWechatInfo());

        // 2. 设置用户名：优先使用微信昵称，如果没有则使用 openid
        String username = loginReq.getNickname();
        if (username == null || username.trim().isEmpty()) {
            username = "wx_" + loginReq.getOpenid().substring(0, Math.min(10, loginReq.getOpenid().length()));
        }
        newUser.setUsername(username);

        // 3. 设置昵称
        newUser.setNickname(loginReq.getNickname());

        // 4. 设置头像
        newUser.setAvatar(loginReq.getAvatar());

        // 5. 设置默认角色和状态
        newUser.setRole("USER");
        newUser.setStatus((byte) 1);  // 1-启用

        // 6. 保存到数据库
        boolean success = userDao.save(newUser);
        if (!success) {
            log.error("Failed to auto register wechat user. openid={}", loginReq.getOpenid());
            throw new BusinessException("微信用户注册失败");
        }

        log.info("Wechat user auto registered successfully. userId={}, username={}, openid={}",
                newUser.getId(), newUser.getUsername(), newUser.getOpenid());

        return newUser;
    }

}
