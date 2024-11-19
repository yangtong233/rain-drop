package org.raindrop.core.auth.handler.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.raindrop.core.auth.handler.PublicResourcesHandler;

/**
 * 默认判断放行请求实现类
 */
@Data
@Accessors(chain = true)
public class DefaultPublicResourcesHandler implements PublicResourcesHandler {

    private PublicResourceParser publicResourceParser;

    @Override
    public boolean handler(HttpServletRequest request, HttpServletResponse response) {
        return publicResourceParser.isPublicResource(request);
    }
}
