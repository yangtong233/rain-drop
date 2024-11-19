package org.raindrop.common.utils.string;

import java.io.IOException;
import java.io.Reader;

/**
 * created by yangtong on 2024/5/18 22:47
 *
 * @Description: 字符串读取器，通过字符流来不断的读取字符串
 */
public class CharReader {
    //字符串缓存大小，每次从流中最多读取BUFFER_SIZE个字符
    private static final int BUFFER_SIZE = 1024;
    //字符串的字符输入流
    private Reader reader;
    //缓存数组
    private char[] buffer;
    //指向buffer缓存的指针，默认0
    private int current;
    //缓存数组里实际有效字符的长度
    private int size;

    public CharReader(Reader reader) {
        this.reader = reader;
        buffer = new char[BUFFER_SIZE];
    }

    /**
     * 返回 current 下标处的字符，不改变current
     *
     * @return
     */
    public char peek() {
        if (current - 1 >= size) {
            return (char) -1;
        }

        return buffer[Math.max(0, current - 1)];
    }

    /**
     * 返回 current 指向的字符，current指针向后移动一位
     *
     * @return
     * @throws IOException
     */
    public char next() throws IOException {
        if (!hasMore()) {
            return (char) -1;
        }

        return buffer[current++];
    }

    /**
     * 指针向前移动一位
     */
    public void back() {
        current = Math.max(0, --current);
    }

    /**
     * 判断字符流中是否还没未读的字符
     *
     * @return
     * @throws IOException
     */
    public boolean hasMore() throws IOException {
        if (current < size) {
            return true;
        }

        fillBuffer();
        return current < size;
    }

    /**
     * 缓存数组buffer里的字节如果读完了，就继续从流中读取字符并填充到buffer里
     *
     * @throws IOException
     */
    void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        //-1表示流中的字符读完了
        if (n == -1) {
            return;
        }
        //从流中读取字符到buffer后，current指针重新置零，size表示实际读取到的字符数
        current = 0;
        size = n;
    }

}
