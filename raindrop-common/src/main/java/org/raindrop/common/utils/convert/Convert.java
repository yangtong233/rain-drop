package org.raindrop.common.utils.convert;

/**
 * created by yangtong on 2024/5/17 20:21
 *
 * @Description: 类型转换动作接口
 */
@FunctionalInterface
public interface Convert<S, T> {

    /**
     * 正向类型转换的执行者
     * @param source 源类型数据
     * @return 转为目标类型后的数据
     */
    T doConvert(S source);

}
