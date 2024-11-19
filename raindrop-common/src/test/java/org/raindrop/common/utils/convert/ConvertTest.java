package org.raindrop.common.utils.convert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.raindrop.common.utils.convert.support.DefaultConvert;
import org.raindrop.common.utils.convert.support.GenericConvert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * created by yangtong on 2024/5/16 21:12
 *
 * @Description: 类型转换器测试类
 */
public class ConvertTest {

    IConvert converter;

    @Before
    public void before() {
        converter = new DefaultConvert();
    }

    /**
     * 测试String转为Integer
     */
    @Test
    public void testStringToInteger() {
        DefaultConvert defaultConverts = new DefaultConvert();
        Integer convert = defaultConverts.convert("12", Integer.class);
        assert convert == 12;
    }

    /**
     * 测试String与LocalDate的转换
     */
    @Test
    public void testStringToLocalDate() {
        //1.测试正序转换,String转为LocalDate
        DefaultConvert defaultConverts = new DefaultConvert();
        LocalDate convert = defaultConverts.convert("2024-12-12", LocalDate.class);
        System.out.println(convert);

        //2.测试逆向转换
        String convert1 = defaultConverts.convert(convert, String.class);
        System.out.println(convert1);
    }

    /**
     * 测试对象转JSON
     */
    @Test
    public void testObjectToJson() {
        DefaultConvert defaultConverts = new DefaultConvert();
//        String convert = defaultConverts.convert(Map.of("name", "张三", "age", 22), String.class);
        String convert = defaultConverts.convert(LocalDate.of(2024, 1, 2), String.class);
        System.out.println(convert);
    }

    /**
     * 测试List和数组的转换
     */
    @Test
    public void testListToArr() {

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Integer[] integers = Converts.convert(list, Integer.class);
        System.out.println(Arrays.toString(integers));

        List<Integer> integerList = Converts.convert(integers);
        System.out.println(integerList);
    }

}
