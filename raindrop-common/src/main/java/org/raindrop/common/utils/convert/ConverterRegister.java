package org.raindrop.common.utils.convert;

/**
 * created by yangtong on 2024/5/16 20:15
 *
 * @Description: 转换器注册表
 */
@FunctionalInterface
public interface ConverterRegister {

    /**
     * 注册类型转换器
     * @param converter
     */
    void registerConverter(Converter<?, ?> converter);

}
