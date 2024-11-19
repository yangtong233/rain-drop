package org.raindrop.core.auth;

import cn.hutool.core.exceptions.ExceptionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.web.ResponseUtil;
import org.raindrop.core.auth.handler.AuthenticateHandler;
import org.raindrop.core.auth.handler.AuthorizeHandler;
import org.raindrop.core.auth.handler.PublicResourcesHandler;

import java.io.IOException;


/**
 * 权限认证过滤器
 */
@Slf4j
@Data
@Accessors(chain = true)
public class AuthFilter implements Filter {
    /**
     * 判断请求资源是否是公共资源的组件
     */
    private PublicResourcesHandler publicResourcesHandler;
    /**
     * 认证组件，判断当前用户是谁
     */
    private AuthenticateHandler authenticateHandler;
    /**
     * 鉴权组件，判断当前用户有没有资格访问目标资源
     */
    private AuthorizeHandler authorizeHandler;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        try {
            //添加跨域(本质是添加响应头)
            ResponseUtil.setCors();
            //如果是预检请求，直接返回
            if (request.getMethod().equals("OPTIONS")) {
                return;
            }
            /*
              该filter的权限校验逻辑如下:
                 1.如果请求的资源为公共资源(publicResourcesHandler.handler返回值为true)，则无需进行后续判断直接放行
                 2.如果为非公共资源，则需要认证当前请求是否登录(authenticateHandler.handler返回值为true)，未登录则拦截请求
                 3.如果已经登录了，则还需要判断当前登录的用户是否有对目标资源的访问权限，没有权限则拦截请求
                 4.放行请求
             */
            if (
                publicResourcesHandler.handler(request, response)     //判断请求是否可以直接放行
                || (authenticateHandler.handler(request, response)     //进行认证
                && authorizeHandler.handler(request, response))       //进行授权
            ) {
                //走到这，说明请求已经通过了权限校验
                filterChain.doFilter(request, response);
            }
        } catch (BaseException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            ResponseUtil.responseByCode((HttpServletResponse)res, e.getCode(), e.getMessage());
        } finally {
            //权限校验过程中使用到的ThreadLocal，都建议在这里remove
            U.remove();
            //添加跨域响应头的操作不能放在这里，因为添加响应头的操作必须在写入响应体之前
        }
    }
}
