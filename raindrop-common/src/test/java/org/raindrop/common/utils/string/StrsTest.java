package org.raindrop.common.utils.string;

import org.junit.Test;
import org.raindrop.common.enums.Placeholder;

import java.util.HashMap;
import java.util.Map;

/**
 * created by yangtong on 2024/5/14 13:13
 *
 * @Description:
 */
public class StrsTest {

    @Test
    public void testRandomStr() {
        System.out.println(Strs.randomStr(10));
        System.out.println(Strs.randomNumber(10));
    }

    @Test
    public void testParseByPlaceholder() {
        String str = "hello #{name, age is #{age}";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "狗剩");
        map.put("age", "233");
        String s = Strs.parseByPlaceholder(str, map,
                Placeholder.LEFT_CURLY_BRACE_PLUS,
                Placeholder.RIGHT_CURLY_BRACE);
        assert "hello #{name, age is 233".equals(s);
    }

    @Test
    public void testFormat1() {
        Map<String, Object> name = new HashMap<>();
        name.put("name", "zs");
        name.put("age", 23);
        assert Strs.format("hello ${name}, age is ${age}", name)
                .equals("hello zs, age is 23");
        assert Strs.format("hello ${name, age is ${age}", name)
                .equals("hello ${name, age is 23");
        assert Strs.format("hello ${name}, age is ${age${$", name)
                .equals("hello zs, age is ${age${$");
    }

    @Test
    public void testFormat2() {
        assert Strs.format("my name is {}, and age is {}", "狗剩", 22).equals("my name is 狗剩, and age is 22");
        assert Strs.format("my name is {}, and age is {}", "狗剩").equals("my name is 狗剩, and age is ");
        assert Strs.format("my name is {}", "狗剩", 22).equals("my name is 狗剩");
    }

}
