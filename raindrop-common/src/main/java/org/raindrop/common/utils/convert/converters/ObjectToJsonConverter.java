package org.raindrop.common.utils.convert.converters;

import cn.hutool.json.JSONUtil;
import org.raindrop.common.utils.convert.Converter;

import java.io.Serializable;

/**
 * created by yangtong on 2024/5/16 21:58
 *
 * @Description: 可序列号对象与JSON字符串的转换，该转换器优先级最低
 */
public class ObjectToJsonConverter implements Converter<Serializable, String> {
    /**
     * 将Object类型数据转为String
     *
     * @param source 源类型数据
     * @return 转为String类型后的数据
     */
    @Override
    public String doConvert(Serializable source) {
        return JSONUtil.toJsonStr(source);
    }

    /**
     * 将String类型数据转为Object
     *
     * @param source 源类型数据
     * @return 转为Object类型后的数据
     */
    @Override
    public Serializable doReverseConvert(String source) {
        return JSONUtil.parseObj(source);
    }

    /**
     * Object是最顶层的类，该转换器优先级应该是最低的
     */
    @Override
    public Integer order() {
        return Integer.MAX_VALUE;
    }
}
