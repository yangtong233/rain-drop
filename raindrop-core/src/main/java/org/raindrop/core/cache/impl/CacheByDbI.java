package org.raindrop.core.cache.impl;

import org.raindrop.core.cache.CacheObjResp;
import org.raindrop.core.cache.ICache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 基于数据库的缓存
 */
@Component("db")
public class CacheByDbI implements ICache {
    @Override
    public List<CacheObjResp> listAll(String key) {
        return List.of();
    }

    @Override
    public <T> T get(String key) {
        return get(key, k -> null);
    }

    @Async
    @Override
    public <T> T get(String key, Function<String, T> defaultValue) {
        return null;
    }

    @Override
    public <T> void set(String key, T value) {

    }

    @Override
    public <T> void set(String key, T value, long expireTime, TimeUnit unit) {

    }

    @Override
    public void delay(String key, long expireTime, TimeUnit unit) {

    }

    @Override
    public void delete(String key) {

    }

}
