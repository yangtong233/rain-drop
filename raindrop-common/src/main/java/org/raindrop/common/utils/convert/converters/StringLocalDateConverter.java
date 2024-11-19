package org.raindrop.common.utils.convert.converters;

import org.raindrop.common.utils.convert.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * created by yangtong on 2024/5/16 21:08
 *
 * @Description: 字符串与LocalDate类型的转换
 */
public class StringLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate doConvert(String source) {
        return LocalDate.parse(source);
    }

    @Override
    public String doReverseConvert(LocalDate source) {
        return source.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public Integer order() {
        return 0;
    }

}
