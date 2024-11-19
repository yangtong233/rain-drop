package org.raindrop.core.web.sys.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登录请求数据模型
 * 用于封装用户登录时的请求参数
 */
@Schema(name = "用户登录请求数据")
@Data
public class LoginReq {
    /**
     * 用户名
     * 用户登录时输入的用户名
     */
    @Schema(name = "用户名")
    private String userName;

    /**
     * 登录密码
     * 用户登录时输入的密码
     */
    @Schema(name = "登录密码")
    private String password;

    /**
     * 验证码
     * 用户在登录过程中需要输入的验证码
     */
    @Schema(name = "验证码")
    private String captcha;

    /**
     * 验证码UUID
     * 验证码的唯一标识符，用于验证验证码的有效性
     */
    @Schema(name = "验证码UUID")
    private String captchaUUID;
}

