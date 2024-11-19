package org.raindrop.common.utils.convert.converters;

import org.raindrop.common.utils.convert.Converter;

import java.math.BigDecimal;

/**
 * created by yangtong on 2024/5/16 21:08
 *
 * @Description: 字符串转为LocalDate类型
 */
public class StringAndBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal doConvert(String source) {
        return new BigDecimal(source);
    }

    @Override
    public String doReverseConvert(BigDecimal source) {
        return source.toString();
    }

    @Override
    public Integer order() {
        return 0;
    }


}
