package org.raindrop.core.auth.handler.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;

/**
 * 记录所有无需认证的资源，公共资源
 */
public class PublicResourceParser extends HashSet<String> {
    //可以对路径进行通配符匹配
    AntPathMatcher matcher = new AntPathMatcher();
    /**
     * 判断当前请求是否可用放行
     * @return true:可用放行;false:需要认证
     */
    public boolean isPublicResource(HttpServletRequest request) {
        for (String url : this) {
            //如果匹配成功，则返回true
            if (matcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

    public PublicResourceParser put(String resource) {
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        super.add(resource);
        return this;
    }
}
