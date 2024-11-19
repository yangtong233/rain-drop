package org.raindrop.common.utils.convert.converters;

import org.raindrop.common.utils.convert.Converter;

import java.util.*;

/**
 * created by yangtong on 2024/5/18 1:06
 *
 * @Description: 集合类型与数组类型的转换器
 */
public class ListToArrayConverter implements Converter<List, Object[]> {

    /**
     * 转换器优先级，数字越低，优先级越高
     *
     * @return
     */
    @Override
    public Integer order() {
        return 0;
    }

    /**
     * 正向类型转换的执行者
     *
     * @param source 源类型数据
     * @return 转为目标类型后的数据
     */
    @Override
    public Object[] doConvert(List source) {
        Object[] arr = new Object[source.size()];
        for (int i = 0; i < source.size(); i++) {
            arr[i] = source.get(i);
        }
        return arr;
    }

    /**
     * 逆向类型转换的执行者
     *
     * @param source 源类型数据
     * @return 转为目标类型后的数据
     */
    @Override
    public List doReverseConvert(Object[] source) {
        List<Object> list = new ArrayList<>(source.length);
        for (int i = 0; i < source.length; i++) {
            list.add(i, source[i]);
        }
        return list;
    }
}
