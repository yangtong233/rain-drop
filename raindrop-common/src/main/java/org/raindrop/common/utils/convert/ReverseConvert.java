package org.raindrop.common.utils.convert;

/**
 * created by yangtong on 2024/5/17 20:19
 *
 * @Description: 逆向类型转换动作接口
 */
@FunctionalInterface
public interface ReverseConvert<T, S> {

    /**
     * 逆向类型转换的执行者
     * @param source 源类型数据
     * @return 转为目标类型后的数据
     */
    S doReverseConvert(T source);

}
