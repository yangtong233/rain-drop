package org.raindrop.core.auth.handler.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raindrop.core.auth.handler.AuthorizeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 授权默认实现
 */
public class DefaultAuthorizeHandler implements AuthorizeHandler {
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response) {
//        HandlerExecutionChain chain;
//        try {
//            chain = handlerMapping.getHandler(request);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        //Object handler = chain.getHandler();
        return true;
    }
}
