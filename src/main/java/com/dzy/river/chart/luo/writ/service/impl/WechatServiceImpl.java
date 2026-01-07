package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.dao.UserDao;
import com.dzy.river.chart.luo.writ.domain.convert.UserConvert;
import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import com.dzy.river.chart.luo.writ.domain.entity.User;
import com.dzy.river.chart.luo.writ.domain.req.WechatLoginReq;
import com.dzy.river.chart.luo.writ.exception.BusinessException;
import com.dzy.river.chart.luo.writ.manager.UserManager;
import com.dzy.river.chart.luo.writ.service.WechatService;
import com.dzy.river.chart.luo.writ.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * 微信服务实现类
 *
 * @author zhuzhiwei
 * @date 2026/01/05
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${wechat.miniapp.appid}")
    private String appid;

    @Value("${wechat.miniapp.secret}")
    private String secret;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConvert userConvert;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserManager userManager;

    private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO wechatLogin(WechatLoginReq wechatLoginReq) {
        // 1. 调用微信API换取openid和session_key
        String openid = getOpenidFromWechat(wechatLoginReq.getCode());

        // 2. 查询用户是否存在
        User user = userDao.selectByOpenid(openid);

        // 3. 如果用户不存在，自动注册
        if (user == null) {
            log.info("Wechat user not found, auto register. openid={}", openid);
            user = autoRegisterWechatUser(openid, wechatLoginReq);
        }

        userManager.checkUser(user);

        // 5. 生成token
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 6. 转换为DTO并设置token
        UserDTO userDTO = userConvert.toUserDTO(user);
        userDTO.setUserId(user.getId());
        userDTO.setToken(accessToken);
        userDTO.setRefreshToken(refreshToken);

        log.info("Wechat login success: userId={}, username={}, openid={}",
                user.getId(), user.getUsername(), user.getOpenid());

        return userDTO;
    }



    /**
     * 调用微信API获取openid
     *
     * @param code 微信登录code
     * @return openid
     */
    private String getOpenidFromWechat(String code) {
        try {
            // 构建请求URL
            String url = String.format(WECHAT_API_URL, appid, secret, code);
            log.info("Calling wechat API: {}", url.replace(secret, "******"));

            // 调用微信API
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            log.info("Wechat API response: {}", response);

            // 解析响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            // 检查错误
            if (jsonNode.has("errcode")) {
                int errcode = jsonNode.get("errcode").asInt();
                if (errcode != 0) {
                    String errmsg = jsonNode.get("errmsg").asText();
                    log.error("Wechat API error: errcode={}, errmsg={}", errcode, errmsg);
                    throw new BusinessException("微信登录失败: " + errmsg);
                }
            }

            // 提取openid
            if (!jsonNode.has("openid")) {
                log.error("Wechat API response missing openid: {}", response);
                throw new BusinessException("微信登录失败: 无法获取openid");
            }

            String openid = jsonNode.get("openid").asText();
            log.info("Got openid from wechat: {}", openid);

            return openid;

        } catch (Exception e) {
            log.error("Failed to call wechat API: {}", e.getMessage(), e);
            throw new BusinessException("微信登录失败: " + e.getMessage());
        }
    }

    /**
     * 自动注册微信用户
     *
     * @param openid         微信openid
     * @param wechatLoginReq
     * @return 创建的用户
     */
    private User autoRegisterWechatUser(String openid, WechatLoginReq wechatLoginReq) {
        User newUser = new User();

        // 1. 设置微信信息
        newUser.setOpenid(openid);

        // 2. 生成用户名（使用openid前缀）
        String username = "wx_" + openid.substring(openid.length() - 10);
        newUser.setUsername(username);

        // 3. 设置默认昵称
        newUser.setNickname(wechatLoginReq.getNickName());
        newUser.setAvatar(wechatLoginReq.getAvatarUrl());

        // 4. 设置默认角色和状态
        newUser.setRole("USER");
        newUser.setStatus((byte) 1);  // 1-启用

        // 5. 保存到数据库
        boolean success = userDao.save(newUser);
        if (!success) {
            log.error("Failed to auto register wechat user. openid={}", openid);
            throw new BusinessException("微信用户注册失败");
        }

        log.info("Wechat user auto registered successfully. userId={}, username={}, openid={}",
                newUser.getId(), newUser.getUsername(), newUser.getOpenid());

        return newUser;
    }
}
