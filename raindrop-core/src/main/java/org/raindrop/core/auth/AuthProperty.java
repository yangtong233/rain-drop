package org.raindrop.core.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by yt on 2024/3/24 22:31
 */
@Data
@Component
@ConfigurationProperties(prefix = "drop.auth")
public class AuthProperty {
    /**
     * 本系统自带的权限认证模块是否被启动
     */
    private Boolean enable;

    /**
     * 用户多久不操作登录会失效，单位s，默认60
     */
    private Long timeOut = 600L;
}
