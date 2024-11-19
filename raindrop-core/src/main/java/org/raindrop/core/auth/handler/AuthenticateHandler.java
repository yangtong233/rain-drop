package org.raindrop.core.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 认证处理器
 */
public interface AuthenticateHandler {
    /**
     * 对请求进行认证的处理器，通常是已经处理已经登录的请求
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return true:认证成功;false认证失败
     */
    boolean handler(HttpServletRequest request, HttpServletResponse response);
}
