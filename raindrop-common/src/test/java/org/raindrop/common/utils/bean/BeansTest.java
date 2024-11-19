package org.raindrop.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Test;

/**
 * created by yangtong on 2024/6/6 21:19:33
 * Bean工具类测试
 */
public class BeansTest {
    //测试属性拷贝
    @Test
    public void testCopy1() {
        A a = new A("zs", 22, 1);
        B b = new B("ls", 24, "女");
        Beans.copy(a, b);
        System.out.println(b);
    }

    @Test
    public void testCopy2() {
        A a = new A("zs!!?", 114514, 1);
        B b = Beans.copy(a, B.class);
        System.out.println(b);
    }

    @AllArgsConstructor
    static class A {
        private String name;
        private Integer age;
        private Integer sex;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class B {
        private String name;
        private Integer age;
        private String sex;
    }
}
