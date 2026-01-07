package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import com.dzy.river.chart.luo.writ.domain.req.WechatLoginReq;

/**
 * 微信服务接口
 *
 * @author zhuzhiwei
 * @date 2026/01/05
 */
public interface WechatService {

    /**
     * 微信小程序登录
     * 使用code换取openid，如果用户不存在则自动注册
     *
     * @param code 微信登录code
     * @return 用户信息（包含token）
     */
    UserDTO wechatLogin(WechatLoginReq wechatLoginReq);
}
