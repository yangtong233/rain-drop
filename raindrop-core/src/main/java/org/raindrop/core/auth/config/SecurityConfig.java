package org.raindrop.core.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.enums.PublicResource;
import org.raindrop.core.auth.AuthFilter;
import org.raindrop.core.auth.handler.AuthenticateHandler;
import org.raindrop.core.auth.handler.AuthorizeHandler;
import org.raindrop.core.auth.handler.PublicResourcesHandler;
import org.raindrop.core.auth.handler.impl.DefaultAuthenticateHandler;
import org.raindrop.core.auth.handler.impl.DefaultAuthorizeHandler;
import org.raindrop.core.auth.handler.impl.DefaultPublicResourcesHandler;
import org.raindrop.core.auth.handler.impl.PublicResourceParser;
import org.raindrop.core.swagger.SpringDocConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * 安全模块的配置
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "drop.auth.enable", havingValue = "true")
public class SecurityConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private SpringDocConfig springDocConfig;

    /**
     * 设置默认的认证处理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticateHandler.class)
    public AuthenticateHandler authenticateHandler() {
        return new DefaultAuthenticateHandler();
    }

    /**
     * 设置默认的授权处理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthorizeHandler.class)
    public AuthorizeHandler authorizeHandler() {
        return new DefaultAuthorizeHandler();
    }

    /**
     * 设置默认的公共资源
     */
    @Bean
    @ConditionalOnMissingBean(PublicResourceParser.class)
    public PublicResourceParser publicResource() {
        PublicResourceParser publicResourceParser = new PublicResourceParser()
                .put(contextPath + "/favicon.ico");
        for (String path : springDocConfig.swaggerPath()) {
            publicResourceParser.put(path);
        }

        Arrays.stream(PublicResource.values())
                .map(resource -> resource.resource)
                .forEach(resource -> publicResourceParser.put(contextPath + resource));

        return publicResourceParser;
    }

    /**
     * 设置默认的公共资源处理器
     */
    @Bean
    @ConditionalOnMissingBean(PublicResourcesHandler.class)
    public PublicResourcesHandler publicResourcesHandler(PublicResourceParser publicResourceParser) {
        DefaultPublicResourcesHandler defaultCommonHandler = new DefaultPublicResourcesHandler();
        defaultCommonHandler.setPublicResourceParser(publicResourceParser);
        return defaultCommonHandler;
    }

    /**
     * 注册权限框架核心过滤器
     */
    @Bean
    public AuthFilter securityRegisterFilter(AuthenticateHandler authenticateHandler, AuthorizeHandler authorizeHandler, PublicResourcesHandler publicResourcesHandler) {
        AuthFilter authFilter = new AuthFilter()
                .setAuthenticateHandler(authenticateHandler)
                .setAuthorizeHandler(authorizeHandler)
                .setPublicResourcesHandler(publicResourcesHandler);
        log.info("~已开启自定义权限认证模块 ☆✲ﾟ｡(((´♡‿♡`+)))｡ﾟ✲☆");
        return authFilter;
    }

}
