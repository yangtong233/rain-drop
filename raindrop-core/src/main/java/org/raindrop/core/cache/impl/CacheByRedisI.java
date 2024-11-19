package org.raindrop.core.cache.impl;

import cn.hutool.json.JSONUtil;
import org.raindrop.core.cache.CacheObjResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.raindrop.core.cache.ICache;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 基于redis的缓存，只有导入了io.lettuce.core依赖，才会加载该缓存
 */
@ConditionalOnClass(name = "io.lettuce.core.RedisClient")
@Component("redis")
public class CacheByRedisI implements ICache {

    @Autowired
    private StringRedisTemplate cache;

    @Override
    public List<CacheObjResp> listAll(String key) {
        return List.of();
    }

    @Override
    public <T> T get(String key) {
        return get(key, k -> null);
    }

    @Override
    public <T> T get(String key, Function<String, T> defaultValue) {
        T value = (T) cache.opsForValue().get(key);
        if (value == null) {
            return defaultValue.apply(key);
        }
        return value;
    }

    @Override
    public <T> void set(String key, T value) {
        cache.opsForValue().set(key, JSONUtil.toJsonStr(value));
    }

    @Override
    public <T> void set(String key, T value, long expireTime, TimeUnit unit) {
        cache.opsForValue().set(key, JSONUtil.toJsonStr(value), expireTime, unit);
    }

    @Override
    public void delay(String key, long expireTime, TimeUnit unit) {
        cache.expire(key, expireTime, unit);
    }

    @Override
    public void delete(String key) {
        cache.delete(key);
    }

}
