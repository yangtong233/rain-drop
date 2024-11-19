package org.raindrop.common.utils.convert;

/**
 * created by yangtong on 2024/5/16 20:10
 *
 * @Description: 类型转换器管理器
 */
@FunctionalInterface
public interface IConvert {

    /**
     * 将S类型的参数source转为T类型
     * @param source 被转换的参数
     * @param targetType 转换后的类型
     * @return 转换后的数据
     * @param <S> 源类型
     * @param <T> 目标类型
     */
    <S, T> T convert(S source, Class<T> targetType);
}
