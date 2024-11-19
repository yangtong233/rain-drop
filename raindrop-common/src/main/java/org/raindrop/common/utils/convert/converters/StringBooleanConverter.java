package org.raindrop.common.utils.convert.converters;

import org.raindrop.common.utils.convert.Converter;
import org.raindrop.common.utils.string.Strs;

import java.util.Date;

/**
 * created by yangtong on 2024/6/5 23:11:15
 */
public class StringBooleanConverter implements Converter<String, Boolean> {
    @Override
    public Integer order() {
        return 0;
    }

    @Override
    public Boolean doConvert(String source) {
        return "true".equalsIgnoreCase(source);
    }

    @Override
    public String doReverseConvert(Boolean source) {
        return String.valueOf(source);
    }
}
