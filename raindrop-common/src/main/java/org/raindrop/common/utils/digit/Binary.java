package org.raindrop.common.utils.digit;

/**
 * created by yangtong on 2024/6/21 下午4:00
 * <p>
 * 二进制
 */
public class Binary {
    //二进制数字
    private final long binaryNumber;

    public Binary(int binaryNumber) {
        this.binaryNumber = binaryNumber;
    }

    /**
     * 使用二进制字符串构造二进制对象
     * @param binary 二进制字符串，例如："01011001"
     */
    public Binary(String binary) {
        this.binaryNumber = Long.parseLong(binary, 2);
    }

    @Override
    public String toString() {
        return Long.toBinaryString(binaryNumber);
    }
}