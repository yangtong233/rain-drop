package org.raindrop.common.utils.convert.support;

import cn.hutool.core.util.ClassUtil;
import org.raindrop.common.KeyValuePair;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.bean.Beans;
import org.raindrop.common.utils.convert.Converter;
import org.raindrop.common.utils.convert.ConverterRegister;
import org.raindrop.common.utils.convert.IConvert;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * created by yangtong on 2024/5/16 20:48
 *
 * @Description: Converts的实现类
 */
public class DefaultConvert implements IConvert, ConverterRegister {

    /**
     * 每一个转换器Converter都会进行两种转换，正序的S转为T，逆向的T转为S
     */
    //正序转换集合
    protected Map<KeyValuePair, Converter> convertsMap = new LinkedHashMap<>();
    //逆序转换集合
    protected Map<KeyValuePair, Converter> reverseConvertsMap = new LinkedHashMap<>();

    {
        //构造时扫描org.raindrop.common.utils.convert.converters下的所有转换器实现类进行注册
        Set<Class<?>> converters = ClassUtil.scanPackageBySuper("org.raindrop.common.utils.convert.converters", Converter.class);
        converters.stream().map(converterClass -> {
            try {
                Constructor<?> constructor;
                constructor = converterClass.getConstructor();
                return  (Converter)constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).sorted(Comparator.comparing(Converter::order)).forEach(this::registerConverter);
    }

    /**
     * 注册类型转换器
     *
     * @param converter
     */
    @Override
    public void registerConverter(Converter<?, ?> converter) {
        KeyValuePair<Class, Class> pair = converter.parseGeneric();
        convertsMap.put(pair, converter);
        reverseConvertsMap.put(pair.reverse(), converter);
    }


    /**
     * 将S类型的参数source转为T类型
     *
     * @param source     被转换的参数
     * @param targetType 转换后的类型
     * @return 转换后的数据
     */
    @Override
    public <S, T> T convert(S source, Class<T> targetType) {
        //1.如果source本来就是targetType类型的，则不用转换
        if (targetType.isAssignableFrom(source.getClass())) {
            return (T)source;
        }

        //2.得到源类型以及目标类型的所有继承链
        List<Class<?>> sourceCandidates = Beans.extendsHierarchy(source.getClass());
        List<Class<?>> targetCandidates = Beans.extendsHierarchy(targetType);

        //3.对sourceCandidates和targetCandidates进行笛卡尔积，并得到对应的KeyValuePair，共m * n中组合
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                KeyValuePair pair = new KeyValuePair<>(sourceCandidate, targetCandidate);
                Converter converter = convertsMap.get(pair);
                if (converter != null) {
                    //进行正序类型转换
                    return (T)converter.convert(source);
                }
                converter = reverseConvertsMap.get(pair);
                if (converter != null) {
                    //逆序类型转换
                    return (T)converter.reverseConvert(source);
                }
            }
        }

        throw new BaseException("从[{}]到[{}]没有合适的类型转换器", source.getClass(), targetType);
    }

}
