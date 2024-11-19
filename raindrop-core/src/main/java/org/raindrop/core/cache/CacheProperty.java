package org.raindrop.core.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "drop.cache")
public class CacheProperty {
    /**
     * 默认缓存实现
     */
    private CacheType type = CacheType.local;

    /**
     * 缓存类型枚举
     */
    enum CacheType {
        /**
         * 使用jvm堆内存作为缓存
         */
        local,
        /**
         * 使用redis作为缓存
         */
        redis,
        /**
         * 使用数据库作为缓存
         */
        db
    }
}
