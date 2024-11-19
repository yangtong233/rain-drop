package org.raindrop.common.utils.convert.converters;

import org.raindrop.common.utils.convert.Converter;

/**
 * created by yangtong on 2024/5/16 20:21
 *
 * @Description: 将String类型转为Integer类型的转换器
 */
public class StringNumberConverter implements Converter<String, Integer> {

    /**
     * 将String类型的源数据source转为Integer
     *
     * @param source 源类型数据
     * @return 转为Integer类型后的数据
     */
    @Override
    public Integer doConvert(String source) {
        return Integer.valueOf(source);
    }

    /**
     * 将Integer类型的源数据source转为String
     *
     * @param source 源类型数据
     * @return 转为String类型后的数据
     */
    @Override
    public String doReverseConvert(Integer source) {
        return String.valueOf(source);
    }

    @Override
    public Integer order() {
        return 0;
    }
}
