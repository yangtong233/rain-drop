package org.raindrop.common.utils.string;

import org.junit.Test;

/**
 * created by yangtong on 2024/5/21 17:55
 *
 * @Description: 字符串迭代器测试类
 */
public class CharIterTest {
    @Test
    public void test() {
        CharIter charIter = new CharIter("hello world!!!my name is xxx!!?");
        char next = charIter.next();
        while (next != (char) -1) {
            System.out.println(next);
            next = charIter.next();
        }
    }
}
