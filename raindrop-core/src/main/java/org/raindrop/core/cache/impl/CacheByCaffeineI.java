package org.raindrop.core.cache.impl;

import com.github.benmanes.caffeine.cache.*;
import lombok.ToString;
import org.raindrop.common.utils.string.Strs;
import org.raindrop.core.cache.CacheObjResp;
import org.raindrop.core.cache.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 基于Caffeine的本地缓存，只有添加了Caffeine依赖才会加载
 */
@ConditionalOnClass(name = "com.github.benmanes.caffeine.cache.Caffeine")
@Component("local")
public class CacheByCaffeineI implements ICache {

    private static final Logger log = LoggerFactory.getLogger(CacheByCaffeineI.class);
    private final Cache<String, CacheValue> cache;

    public CacheByCaffeineI() {
        cache = Caffeine.newBuilder()
                .scheduler(Scheduler.systemScheduler())
                .maximumSize(1_0000)
                .expireAfter(new BaseExpiry())
                .recordStats()
                .removalListener((key, value, cause) -> {
                    //当有数据删除时的回调函数
                    log.trace("{}被删除了{}  {}", key, value, cause);
                })
                .build();
    }

    @Override
    public List<CacheObjResp> listAll(String key) {
        ConcurrentMap<String, CacheValue> map = cache.asMap();
        return map.entrySet().stream().map(kv -> new CacheObjResp()
                .setKey(kv.getKey())
                .setValue(kv.getValue().value)
                .setType(kv.getValue().type)
                .setCreateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(kv.getValue().createTime), ZoneId.of("Asia/Chongqing")))
                .setTimeout(kv.getValue().expireTimeUnit.toSeconds(kv.getValue().expireTime))
        ).filter(item -> {
            if (Strs.isNotEmpty(key)) {
                return item.getKey().contains(key);
            }
            return true;
        }).toList();
    }

    @Override
    public <T> T get(String key) {
        return get(key, k -> null);
    }

    @Override
    public <T> T get(String key, Function<String, T> defaultValue) {
        CacheValue<T> cacheValue = cache.getIfPresent(key);
        if (cacheValue == null) {
            return defaultValue.apply(key);
        }
        return cacheValue.value;
    }

    @Override
    public <T> void set(String key, T value) {
        set(key, value, Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    @Override
    public <T> void set(String key, T value, long expireTime, TimeUnit unit) {
        CacheValue<T> cacheValue = new CacheValue<>();
        cacheValue.value = value;
        cacheValue.type = value.getClass();
        cacheValue.expireTimeUnit = unit;
        cacheValue.expireTime = expireTime;
        cacheValue.createTime = System.currentTimeMillis();
        cache.put(key, cacheValue);
    }

    @Override
    public void delay(String key, long expireTime, TimeUnit unit) {
        CacheValue cacheValue = cache.getIfPresent(key);
        if (cacheValue != null) {
            set(key, cacheValue.value, expireTime, unit);
        }
    }

    @Override
    public void delete(String key) {
        cache.invalidate(key);
    }

    /**
     * 将缓存数据封装到CacheValue
     *
     * @param <T>
     */
    @ToString
    class CacheValue<T> {

        /**
         * 真实的缓存数据
         */
        private T value;
        /**
         * 缓存数据类型
         */
        private Class type;
        /**
         * 创建时间
         */
        private long createTime;
        /**
         * 过期时间单位
         */
        private TimeUnit expireTimeUnit;
        /**
         * 过期时间
         */
        private long expireTime;
    }

    //过期策略
    class BaseExpiry implements Expiry<String, CacheValue> {
        public long expireAfterCreate(String key, CacheValue value, long currentTime) {
            return value.expireTimeUnit.toNanos(value.expireTime);
        }

        public long expireAfterUpdate(String key, CacheValue value, long currentTime, long currentDuration) {
            return value.expireTimeUnit.toNanos(value.expireTime);
        }

        public long expireAfterRead(String key, CacheValue value, long currentTime, long currentDuration) {
            return currentDuration;
        }
    }
}
