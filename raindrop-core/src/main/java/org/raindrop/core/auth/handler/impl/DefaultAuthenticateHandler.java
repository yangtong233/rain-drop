package org.raindrop.core.auth.handler.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raindrop.common.enums.Resp;
import org.raindrop.common.exception.BaseException;
import org.raindrop.core.auth.handler.AuthenticateHandler;
import org.raindrop.core.cache.C;
import org.raindrop.core.auth.U;
import org.raindrop.core.web.rabc.model.resp.OnlineUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 认证默认实现
 */
public class DefaultAuthenticateHandler implements AuthenticateHandler, ApplicationContextAware {

    private Long timeOut;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        timeOut = context.getEnvironment().getProperty("drop.auth.time-out", Long.class);
    }

    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response) {
        //1.先看请求头中有没有一个叫Authorization的请求头
        String authorization = request.getHeader("Authorization");
        //2.没有的话再看有没有cookie
        if (authorization == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                cookies = new Cookie[0];
            }
            Map<String, String> map = Arrays.stream(cookies).collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
            //cookie中没有id，则认为当前未登录
            if (!map.containsKey("Authorization")) {
                throw new BaseException(Resp.NOT_LONGIN);
            }
            authorization = map.get("Authorization");
        }

        //3.根据令牌查看缓存有没有该数据
        OnlineUser onlineUser = C.get("onlineUser::" + authorization);
        //4.缓存中没有当前令牌信息，说明登录过期，用户信息已经从缓存中被清除了
        if (onlineUser == null) {
            throw new BaseException(Resp.LONGIN_TIME_OUT);
        }
        //5.走到这，说明当前用户已经登录了，认证通过，更新当前用户的访问信息
        onlineUser.setLastRequestTime(LocalDateTime.now());
        onlineUser.setRequestCount(onlineUser.getRequestCount() + 1);
        C.set("onlineUser::" + authorization, onlineUser, timeOut, TimeUnit.SECONDS);

        //6.设置当前用户上下文
        U.set(onlineUser);
        return true;
    }
}
