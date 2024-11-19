package org.raindrop.core.cache.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.exception.BaseException;
import org.raindrop.core.cache.CacheObjResp;
import org.raindrop.core.cache.ICache;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 基于本地内存的缓存，本质就是一个map
 */
@Order(Integer.MIN_VALUE)
@Slf4j
//@Component
@Deprecated
public class CacheByLocalI implements ICache {
    private Map<String, CacheObj> cacheMap = new ConcurrentHashMap<>();
    private Thread cleanCacheThread;
    private static AtomicInteger threadNum = new AtomicInteger(0);
    private Long cacheSize;

    public CacheByLocalI() {
        int num = threadNum.incrementAndGet();
        //创建一个线程定时清理缓存的过期key
        cleanCacheThread = new Thread(() -> {
            try {
                while (true) {
                    TimeUnit.MINUTES.sleep(1);
                    long expireKeyCount = 0;
                    long size = 0;
                    for (CacheObj cache : cacheMap.values()) {
                        if (cache.clearExpireKey()) {
                            expireKeyCount++;
                        } else {
                            size += cache.value == null ? 4 : cache.value.length();
                            size += cache.key.length();
                            size += 8;
                        }
                    }
                    cacheSize = size;
                    log.debug("清除过期key的数量:{}", expireKeyCount);
                    log.debug("本地缓存占用内存{}个字节", size);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        cleanCacheThread.start();
    }

    @Override
    public List<CacheObjResp> listAll(String key) {
        return List.of();
    }

    @Override
    public <T> T get(String key) {
        return get(key, e -> null);
    }

    @Override
    public <T> T get(String key, Function<String, T> defaultValue) {
        //通过":"来分割key
        String[] keySplit = key.split("::", 2);
        //如果无法分割，则直接根据key获取value
        if (keySplit.length == 1) {
            CacheObj<T> cacheObj = cacheMap.get(key);
            if (cacheObj == null) {
                return defaultValue.apply(key);
            }
            return cacheObj.<T>getValue();
        }
        //如果可以分割，则说明value又是一个cache，拿分割后的key从这个cache里得到值。理论可以无限嵌套cache
        else {
            CacheObj<CacheByLocalI> outerCache = cacheMap.get(keySplit[0]);
            if (outerCache == null) {
                return defaultValue.apply(key);
            }
            CacheByLocalI localCache = outerCache.getValue();
            return localCache.<T>get(keySplit[1], defaultValue);
        }
    }

    public <T> void set(String key, T value) {
        set(key, value, Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> void set(String key, T value, long expireTime, TimeUnit unit) {
        boolean flag = false;
        String[] keySplit = key.split("::", 2);
        if (keySplit.length == 1) {
            put(key, value, expireTime, unit, flag);
        }
        else {
            CacheByLocalI localCache = get(keySplit[0]);
            if (localCache != null) {
                localCache.set(keySplit[1], value, expireTime, unit);
            }
            else {
                localCache = new CacheByLocalI();
                localCache.set(keySplit[1], value, expireTime, unit);
                put(keySplit[0], localCache, expireTime, unit, true);
            }
        }
    }

    @Override
    public void delay(String key, long expireTime, TimeUnit unit) {
        CacheObj cacheObj = cacheMap.get(key);
        if (cacheObj != null) {
            cacheObj.expireTime = System.currentTimeMillis() + unit.toMillis(expireTime);
        }
    }

    @Override
    public void delete(String key) {
        cacheMap.remove(key);
    }

    /**
     * 将value序列化后进行存放
     * @param key
     * @param value
     * @param expireTime 过期时间大小
     * @param unit  过期时间单位
     * @param origin 是否存原始对象
     * @param <T>  value类型
     */
    private <T> void put(String key, T value, long expireTime, TimeUnit unit, boolean origin) {
        if (value == null) {
            throw new BaseException("key{} -> 不应该放入一个空值", key);
        }
        CacheObj<T> cacheObj = new CacheObj<>();
        cacheObj.key = key;
        //存入缓存的是原始数据(共享)
        if (origin) {
            cacheObj.originValue = value;
        }
        //存入缓存的是原始数据(非共享)
        else {
            cacheObj.clazz = (Class<T>) value.getClass();
            //是基本类型，转化为对应字符串格式
            if (value instanceof Number || value instanceof Boolean || value instanceof Character) {
                cacheObj.value = value.toString();
            }
            //是非基本类型，转化为json字符串格式
            else {
                cacheObj.value = JSONUtil.toJsonStr(value);
            }
        }
        cacheObj.expireTime = Long.max(System.currentTimeMillis() + unit.toMillis(expireTime), unit.toMillis(expireTime));
        cacheMap.put(key, cacheObj);
    }

    /**
     * 缓存对象
     * @param <T> 缓存对象保存的value类型
     */
    class CacheObj<T> {
        private String key;
        //序列化后的value
        private String value;
        //原始对象
        private T originValue;
        //value的类型
        private Class<T> clazz;
        //过期时间
        private long expireTime;

        public T getValue() {
            if (clearExpireKey()) {
                return null;
            }
            //根据clazz字段是否为空来判断存的是原始数据还是序列化数据
            if (clazz == null) {
                return originValue;
            }
            else {
                //如果value是数值类型
                if (Byte.class.equals(clazz) || Short.class.equals(clazz) || Integer.class.equals(clazz)
                        || Long.class.equals(clazz) || Double.class.equals(clazz) || Float.class.equals(clazz)) {
                    return (T)Convert.toNumber(value);
                }
                //如果是大数类型
                else if (BigDecimal.class.equals(clazz)) {
                    return (T)Convert.toBigDecimal(value);
                }
                //如果value是boolean类型
                else if (Boolean.class.equals(clazz)) {
                    return (T)Convert.toBool(value);
                }
                //如果value是char类型
                else if (Character.class.equals(clazz)) {
                    return (T)Convert.toChar(value);
                }
                //如果是字符串类型
                else if (String.class.equals(clazz)) {
                    return (T)value;
                }
                //如果value是对象类型(非基本类型和包装类型)
                return JSONUtil.toBean(value, clazz);
            }
        }

        /**
         * 清除过期key
         * @return 该对象的key是否过期
         */
        public boolean clearExpireKey() {
            if (System.currentTimeMillis() >= expireTime) {
                cacheMap.remove(key);
                return true;
            }
            return false;
        }
    }

}
