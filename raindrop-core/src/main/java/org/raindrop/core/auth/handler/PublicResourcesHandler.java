package org.raindrop.core.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 公共资源处理器
 */
public interface PublicResourcesHandler {
    /**
     * 判断请求是否可用直接放行
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return true:公共资源，直接放行;false:需要认证
     */
    boolean handler(HttpServletRequest request, HttpServletResponse response);
}
