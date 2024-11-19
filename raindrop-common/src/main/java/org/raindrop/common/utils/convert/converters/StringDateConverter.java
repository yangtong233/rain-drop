package org.raindrop.common.utils.convert.converters;

import cn.hutool.core.date.DateUtil;
import org.raindrop.common.utils.convert.Converter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * created by yangtong on 2024/6/5 22:06:44
 * @Description: 字符串与Date类型相互转换
 */
public class StringDateConverter implements Converter<String, Date> {
    @Override
    public Integer order() {
        return 0;
    }

    @Override
    public Date doConvert(String source) {
        return DateUtil.parse(source);
    }

    @Override
    public String doReverseConvert(Date source) {
        return DateUtil.format(source, "yyyy-MM-dd");
    }
}
