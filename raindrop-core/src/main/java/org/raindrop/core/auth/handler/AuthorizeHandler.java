package org.raindrop.core.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raindrop.common.exception.BaseException;

/**
 * 授权处理器
 */
public interface AuthorizeHandler {

    /**
     * 对请求进行授权
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return true:授权成功;false:授权失败
     */
    boolean handler(HttpServletRequest request, HttpServletResponse response) throws BaseException;
}
