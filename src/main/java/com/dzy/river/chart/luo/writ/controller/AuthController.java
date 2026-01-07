package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.common.Result;
import com.dzy.river.chart.luo.writ.domain.dto.UserDTO;
import com.dzy.river.chart.luo.writ.domain.req.WechatLoginReq;
import com.dzy.river.chart.luo.writ.service.WechatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理登录、注册等认证相关接口
 *
 * @author zhuzhiwei
 * @date 2026/01/05
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "用户认证", description = "登录、注册等认证相关接口")
@Validated
public class AuthController {

    @Autowired
    private WechatService wechatService;

    /**
     * 微信小程序登录
     */
    @PostMapping("/wechat-login")
    @Operation(summary = "微信小程序登录", description = "使用微信登录code进行登录，首次登录自动注册")
    public Result<UserDTO> wechatLogin(@RequestBody @Validated WechatLoginReq req) {
        UserDTO userDTO = wechatService.wechatLogin(req);
        return Result.success("登录成功", userDTO);
    }
}
