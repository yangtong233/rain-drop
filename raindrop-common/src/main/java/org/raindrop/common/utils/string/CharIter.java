package org.raindrop.common.utils.string;

import java.io.IOException;

/**
 * created by yangtong on 2024/5/21 17:48
 *
 * @Description: 字符串读取器，直接基于字符数组来不断的读取字符串
 */
public class CharIter {

    //字符数组
    private final char[] buffer;
    //指向buffer缓存的指针，默认0
    private int current;

    public CharIter(String targetStr) {
        buffer = targetStr.toCharArray();
    }

    /**
     * 返回 current 下标处的字符，不改变current
     *
     * @return
     */
    public char peek() {
        if (current >= buffer.length) {
            return (char) -1;
        }

        return buffer[Math.max(0, current)];
    }

    /**
     * 返回 current 指向的字符，current指针向后移动一位
     *
     * @return
     * @throws IOException
     */
    public char next() {
        if (!hasMore()) {
            return (char) -1;
        }

        return buffer[current++];
    }

    /**
     * 指针向前回推移动一位
     */
    public void back() {
        current = Math.max(0, --current);
    }

    /**
     * 判断是否还有尚未读取的字符
     *
     * @return
     * @throws IOException
     */
    public boolean hasMore() {
        return current < buffer.length;
    }

}
