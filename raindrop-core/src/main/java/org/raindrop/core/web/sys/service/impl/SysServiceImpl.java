package org.raindrop.core.web.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.enums.Resp;
import org.raindrop.common.exception.BaseException;
import org.raindrop.core.auth.password.Identity;
import org.raindrop.core.cache.C;
import org.raindrop.core.web.rabc.model.po.SysUser;
import org.raindrop.core.web.rabc.model.resp.OnlineUser;
import org.raindrop.core.web.rabc.model.resp.SysUserDetailResp;
import org.raindrop.core.web.sys.model.req.LoginReq;
import org.raindrop.core.web.rabc.service.SysPermissionService;
import org.raindrop.core.web.rabc.service.SysRoleService;
import org.raindrop.core.web.rabc.service.SysUserService;
import org.raindrop.core.web.sys.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysServiceImpl implements ISysService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private Identity identity;
    @Value("${drop.auth.time-out}")
    private Long timeOut;

    @Override
    public SysUserDetailResp login(LoginReq userLogin) {
        //1.对验证码进行校验
        String captcha = C.get(userLogin.getCaptchaUUID());
        if (!userLogin.getCaptcha().equalsIgnoreCase(captcha)) {
            throw new BaseException(Resp.CAPTCHA_ERROR);
        }
        C.delete(userLogin.getCaptchaUUID());

        //2.对账号密码进行校验
        SysUser user = userService.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUserName, userLogin.getUserName()));
        if (user == null) {
            throw new BaseException(Resp.UNKNOWN_USER);
        }
        if (!user.getStatus()) {
            throw new BaseException(Resp.BANNED_USER);
        }
        if (!identity.matchPassword(userLogin.getPassword(), user.getPassword())) {
            throw new BaseException(Resp.PASSWORD_ERROR);
        }

        //3.登录成功，查询该登录用户的详细信息
        SysUserDetailResp detail = userService.getDetailByUser(user);

        //4.生成令牌发给前端，并保存当前登录用户的信息
        String token = IdUtil.fastSimpleUUID();
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        //cookie.setMaxAge(Integer.valueOf(timeOut.toString()));
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse res = attrs.getResponse();
        res.addCookie(cookie);

        OnlineUser onlineUser = BeanUtil.copyProperties(detail, OnlineUser.class);
        onlineUser.setUserId(detail.getId());
        onlineUser.setLoginTime(LocalDateTime.now());
        onlineUser.setToken(token);
        C.set("onlineUser::" + token, onlineUser, timeOut, TimeUnit.SECONDS);

        log.info("{}登录成功", user.getUserName());
        return detail;
    }
}
