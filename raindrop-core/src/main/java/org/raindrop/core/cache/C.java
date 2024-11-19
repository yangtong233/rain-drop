package org.raindrop.core.cache;

import org.raindrop.common.exception.BaseException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 以静态方式操作缓存
 */
@Component
public class C {
    private static CacheService cacheService = null;

    public C(CacheService cacheService) {
        C.cacheService = cacheService;
    }

    /**
     * 返回缓存中的符合条件的所有数据
     * @param key 缓存键
     * @return 符合条件的缓存
     */
    public static List<CacheObjResp> listAll(String key) {
        check();
        return cacheService.listAll(key);
    }

    /**
     * 根据key获取value
     *
     * @param key 要获取的key
     * @param <T> 返回值的类型
     * @return 返回值
     */
    public static <T> T get(String key) {
        check();
        return cacheService.get(key);
    }

    /**
     * 根据key获取value，如果没获取到则返回默认值
     *
     * @param key 要获取的key
     * @param <T> 返回值的类型
     * @return 返回值
     */
    public static <T> T get(String key, Function<String, T> defaultValue) {
        check();
        return cacheService.get(key, defaultValue);
    }

    /**
     * 添加一份数据
     *
     * @param key  key
     * @param value value
     * @param <T>   value的类型
     */
    public static <T> void set(String key, T value) {
        check();
        cacheService.set(key, value);
    }

    /**
     * 添加一份带有过期时间的数据
     *
     * @param key key
     * @param value value
     * @param expireTime 过期时间的大小
     * @param unit       过期时间的单位
     * @param <T>        value类型
     */
    public static <T> void set(String key, T value, long expireTime, TimeUnit unit) {
        check();
        cacheService.set(key, value, expireTime, unit);
    }

    /**
     * 给指定key重置过期时间
     *
     * @param key key
     * @param expireTime 过期时间的大小
     * @param unit       过期时间的单位
     */
    public static void delay(String key, long expireTime, TimeUnit unit) {
        check();
        cacheService.delay(key, expireTime, unit);
    }

    /**
     * 根据key删除数据
     *
     * @param key 要删除的key
     */
    public static void delete(String key) {
        check();
        cacheService.delete(key);
    }

    /**
     * 使用C的静态方法之前，检测cacheManger是否被注入
     */
    private static void check() {
        if (cacheService == null) {
            throw new BaseException("确保在spring上下文中使用C类的静态方法");
        }
    }
}
