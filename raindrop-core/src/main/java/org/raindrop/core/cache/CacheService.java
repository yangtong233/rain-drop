package org.raindrop.core.cache;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.enums.Resp;
import org.raindrop.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对各个缓存实现类进行统一管理
 */
@Component
@Slf4j
public class CacheService {

    /**
     * 配置文件中指定的默认缓存实现类，如果没有指定则使用local缓存
     */
    private ICache defaultCache;

    @Autowired
    public void init(ApplicationContext context, CacheProperty cacheProperty) {
        String[] beanNames = context.getBeanNamesForType(ICache.class);
        Map<CacheProperty.CacheType, ICache> cacheMap = Arrays.stream(beanNames)
                .collect(Collectors.toMap(CacheProperty.CacheType::valueOf, beanName -> context.getBean(beanName, ICache.class)));
        if (!cacheMap.containsKey(cacheProperty.getType())) {
            defaultCache = cacheMap.get(CacheProperty.CacheType.local);
            log.error("指定的缓存实现类\"{}\"不存在，将使用默认的local缓存", cacheProperty.getType());
        } else {
            defaultCache = cacheMap.get(cacheProperty.getType());
            log.info("使用\"{}\"作为默认缓存~", cacheProperty.getType());
        }
    }

    /**
     * 返回缓存中的符合条件的所有数据
     * @param key 缓存键
     * @return 符合条件的缓存
     */
    List<CacheObjResp> listAll(String key) {
        return defaultCache.listAll(key);
    }

    /**
     * 根据key获取value
     *
     * @param key 要获取的key
     * @param <T> 返回值的类型
     * @return 返回值
     */
    <T> T get(String key) {

        if (StrUtil.isEmpty(key)) {
            throw new BaseException(Resp.NULL_KEY);
        }
        return defaultCache.get(key);
    }

    /**
     * 根据key获取value，如果没获取到则返回默认值
     *
     * @param key 要获取的key
     * @param <T> 返回值的类型
     * @return 返回值
     */
    <T> T get(String key, Function<String, T> defaultValue) {
        if (StrUtil.isEmpty(key)) {
            throw new BaseException(Resp.NULL_KEY);
        }
        return defaultCache.get(key, defaultValue);
    }

    /**
     * 添加一份数据
     *
     * @param key key
     * @param value value
     * @param <T>   value的类型
     */
    <T> void set(String key, T value) {
        defaultCache.set(key, value);
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
    <T> void set(String key, T value, long expireTime, TimeUnit unit) {
        defaultCache.set(key, value, expireTime, unit);
    }

    /**
     * 给指定key重置过期时间
     *
     * @param key key
     * @param expireTime 过期时间的大小
     * @param unit       过期时间的单位
     */
    void delay(String key, long expireTime, TimeUnit unit) {
        defaultCache.delay(key, expireTime, unit);
    }

    /**
     * 根据key删除数据
     *
     * @param key 要删除的key
     */
    void delete(String key) {
        defaultCache.delete(key);
    }
}
