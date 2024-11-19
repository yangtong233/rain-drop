package org.raindrop.common.utils.digit;

/**
 * created by yangtong on 2024/6/24 上午9:58
 * <p>
 * 十六进制
 */
public class Hexadecimal {
    //十六进制数字
    private final long number;

    //对应的十进制数字
    private long decimal;

    public Hexadecimal(long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return Long.toHexString(number);
    }
}
