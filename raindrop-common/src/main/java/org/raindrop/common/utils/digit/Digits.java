package org.raindrop.common.utils.digit;

import org.raindrop.common.exception.BaseException;

/**
 * 位运算工具类
 */
public class Digits {

    private static DecimalToOther decimal;

    public static String _10To16(byte[] data) {
        if (decimal == null) {
            decimal = new DecimalToOther();
        }
        return decimal.to16(data);
    }

    /**
     * 位运算，判断number中是否包含目标数字target
     */
    public boolean contain(int number, int target) {
        if (isPowerOfTwo(number) && isPowerOfTwo(target)) {
            return (number & target) == target;
        }
        throw new BaseException("数字必须是2的次幂");
    }

    /**
     * 位运算，向number添加目标数字target
     */
    public int add(int number, int target) {
        if (isPowerOfTwo(number) && isPowerOfTwo(target)) {
            return number | target;
        }
        throw new BaseException("数字必须是2的次幂");
    }

    /**
     * 位运算，从number移除目标数字target
     */
    public int remove(int number, int target) {
        if (isPowerOfTwo(number) && isPowerOfTwo(target)) {
            return number & ~target;
        }
        throw new BaseException("数字必须是2的次幂");
    }

    /**
     * 位运算，求余
     */
    public int surplus(int dividend, int divisor) {
        if (isPowerOfTwo(divisor)) {
            return dividend & (divisor - 1);
        }
        throw new BaseException("除数必须是2的次幂");
    }

    /**
     * 检查目标数字target是否为2的次幂
     */
    public boolean isPowerOfTwo(int target) {
        return (target & (target - 1)) == 0;
    }
}