package com.dzy.river.chart.luo.writ.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求
 *
 * @author zhuzhiwei
 * @date 2026/01/04
 */
@Data
@Schema(description = "登录请求")
public class LoginReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名（用户名密码登录时必填）", example = "zhangsan")
    private String username;

    @Schema(description = "密码（用户名密码登录时必填）", example = "123456")
    private String password;

    @Schema(description = "微信openid（微信登录时必填）", example = "wx_openid_123")
    private String openid;

    @Schema(description = "微信unionid（微信登录时可选）", example = "wx_unionid_456")
    private String unionid;

    @Schema(description = "微信昵称（微信登录时推荐提供，用于自动注册）", example = "张三")
    private String nickname;

    @Schema(description = "微信头像URL（微信登录时可选）", example = "https://xxx.com/avatar.jpg")
    private String avatar;

    @Schema(description = "微信用户完整信息JSON（微信登录时可选）", example = "{\"nickName\":\"张三\",\"gender\":1}")
    private String wechatInfo;
}
