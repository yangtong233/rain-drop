package org.raindrop.core.web.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.raindrop.common.web.R;
import org.raindrop.core.cache.C;
import org.raindrop.core.web.rabc.model.po.SysUser;
import org.raindrop.core.web.rabc.model.resp.SysUserDetailResp;
import org.raindrop.core.web.sys.service.ISysService;
import org.raindrop.core.web.sys.model.req.LoginReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sys")
@Tag(name = "系统登录接口")
public class SysController {

    @Autowired
    private ISysService sysService;

    @GetMapping("/getCaptcha")
    @Operation(summary = "得到图片验证码")
    public R<JSONObject> getCaptcha() {
        //生成该图片验证码对应的uuid
        String uuid = UUID.randomUUID().toString();
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 10);
        //将验证码加入缓存
        C.set(uuid, captcha.getCode(), 1L, TimeUnit.MINUTES);
        JSONObject json = new JSONObject();
        json.putOnce("uuid", uuid);
        json.putOnce("base64ImgStr", captcha.getImageBase64());
        return R.success(json);
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public R<String> register(@RequestBody SysUser user) {
        return R.success("操作成功");
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public R<SysUserDetailResp> login(@RequestBody LoginReq userLogin) {
        SysUserDetailResp userDetail = sysService.login(userLogin);
        return R.success(userDetail);
    }

    @PostMapping("/logout")
    @Operation(summary = "注销")
    public R<String> logout(@CookieValue(value = "Authorization", required = false) String token) {
        C.delete("onlineUser::" + token);
        return R.success();
    }
}
