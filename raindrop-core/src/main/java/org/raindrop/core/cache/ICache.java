package org.raindrop.core.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存接口规范
 */
public interface ICache {

    /**
     * 返回缓存中的符合条件的所有数据
     * @param key 缓存键
     * @return
     */
    List<CacheObjResp> listAll(String key);

    /**
     * 根据key获取value
     * @param key
     * @param <T> 返回值的类型
     * @return
     */
    <T> T get(String key);

    /**
     * 根据key获取value，如果没获取到则返回默认值
     * @param key
     * @param <T> 返回值的类型
     * @return
     */
    <T> T get(String key, Function<String, T> defaultValue);

    /**
     * 添加一份数据
     * @param key
     * @param value
     * @param <T> value的类型
     */
    <T> void set(String key, T value);

    /**
     * 添加一份带有过期时间的数据
     * @param key
     * @param value
     * @param expireTime 过期时间的大小
     * @param unit  过期时间的单位
     * @param <T> value类型
     */
    <T> void set(String key, T value, long expireTime, TimeUnit unit);

    /**
     * 给指定key重置过期时间
     * @param key
     * @param expireTime 过期时间的大小
     * @param unit 过期时间的单位
     */
    void delay(String key, long expireTime, TimeUnit unit);

    /**
     * 根据key删除数据
     * @param key
     */
    void delete(String key);
}
