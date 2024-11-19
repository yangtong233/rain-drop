package org.raindrop.common.utils.convert.support;

import org.raindrop.common.KeyValuePair;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.convert.Converter;

/**
 * created by yangtong on 2024/5/18 1:49
 *
 * @Description:
 */
public class GenericConvert extends DefaultConvert {

    @Override
    public <S, T> T convert(S source, Class<T> targetType) {
        if (targetType.isAssignableFrom(source.getClass())) {
            return (T) source;
        }

        for (KeyValuePair pair : convertsMap.keySet()) {
            Class sourceClass = (Class) pair.key();
            Class targetClass = (Class) pair.value();
            if (sourceClass.isAssignableFrom(source.getClass()) && targetClass.isAssignableFrom(targetType)) {
                Converter converter = convertsMap.get(pair);
                return (T) converter.convert(source);
            } else if (targetClass.isAssignableFrom(source.getClass()) && sourceClass.isAssignableFrom(targetType)) {
                Converter converter = reverseConvertsMap.get(pair);
                return (T) converter.reverseConvert(source);
            }
        }

        throw new BaseException("从[{}]到[{}]没有合适的类型转换器", source.getClass(), targetType);
    }
}
