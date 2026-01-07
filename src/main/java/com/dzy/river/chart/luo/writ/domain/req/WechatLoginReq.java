package com.dzy.river.chart.luo.writ.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信登录请求
 *
 * @author zhuzhiwei
 * @date 2026/01/05
 */
@Data
@Schema(description = "微信登录请求")
public class WechatLoginReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "微信登录code不能为空")
    @Schema(description = "微信登录code（由uni.login()获取）", required = true, example = "0x1abc2def3ghi")
    private String code;


    @Schema(description = "微信头像URL", example = "https://example.com/avatar.png")
    private String avatarUrl;

    @Schema(description = "微信昵称", example = "张三")
    private String nickName;
}
