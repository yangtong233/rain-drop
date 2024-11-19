package org.raindrop.common.utils.convert;

import org.raindrop.common.utils.convert.support.DefaultConvert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * created by yangtong on 2024/5/16 21:37
 *
 * @Description: 类型转换器工具类
 */
public class Converts {

    static final IConvert convert;
    static {
        convert = new DefaultConvert();
    }

    //进行类型转换
    public static <S, T> T convert(S source, Class<T> targetType) {
        return convert.convert(source, targetType);
    }

    //类型数组类型转换
    public static <T> T[] convert(List<T> sources, Class<T> targetArrClass) {
        T[] arr = (T[]) Array.newInstance(targetArrClass, sources.size());
        for (int i = 0; i < sources.size(); i++) {
            arr[i] = sources.get(i);
        }
        return arr;
    }

    public static <T> List<T> convert(T[] arrSources) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < arrSources.length; i++) {
            list.add(i, arrSources[i]);
        }
        return list;
    }
}
