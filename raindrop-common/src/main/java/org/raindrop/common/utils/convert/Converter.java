package org.raindrop.common.utils.convert;

import org.raindrop.common.KeyValuePair;
import org.raindrop.common.exception.BaseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * created by yangtong on 2024/5/16 20:05
 *
 * @Description: 类型转换器，可以进行两种转换，正序的S转为T，逆向的T转为S
 */
public interface Converter<S, T> extends Convert<S, T>, ReverseConvert<T, S> {

    /**
     * 进行正向类型转换，从S到T
     * @param source 源类型数据
     * @return 转为T类型后的数据
     */
    default T convert(S source) {
        if (source == null) {
            return null;
        }
        try {
            return doConvert(source);
        } catch (Exception e) {
            throw new BaseException("转换失败", e);
        }
    }

    /**
     * 进行逆向类型转换，从T到S
     * @param source 源类型数据
     * @return 转为T类型后的数据
     */
    default S reverseConvert(T source) {
        if (source == null) {
            return null;
        }
        try {
            return doReverseConvert(source);
        } catch (Exception e) {
            throw new BaseException("转换失败", e);
        }
    }

    /**
     * 解析当前转换器的S、T泛型的真实类型并封装为KeyValuePair对象
     * @return
     */
    default KeyValuePair<Class, Class> parseGeneric() {
        //获取当前转换器实现的接口
        Type[] interfaces = this.getClass().getGenericInterfaces();
        for (Type interfaceType : interfaces) {
            //ParameterizedType表示带泛型的类型，如果实现的接口带泛型并且接口是Converter类型
            if (interfaceType instanceof ParameterizedType converter && converter.getRawType().equals(Converter.class)) {
                //就得到接口的两个泛型类型
                Type[] actualTypeArguments = converter.getActualTypeArguments();
                Class sourceType = (Class) actualTypeArguments[0];
                Class targetType = (Class) actualTypeArguments[1];
                return new KeyValuePair<>(sourceType, targetType);
            }
        }
        return null;
    }

    /**
     * 转换器优先级，数字越低，优先级越高
     * @return
     */
    Integer order();
}
