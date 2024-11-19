package org.raindrop.common.utils.excel;

import org.junit.Test;
import org.raindrop.common.enums.FileType;
import org.raindrop.common.utils.bean.Beans;
import org.raindrop.common.utils.excel.support.TemplateFormExcelWriter;
import org.raindrop.common.utils.excel.support.TemplateTableExcelWriter;
import org.raindrop.common.utils.excel.support.TableExcelWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excels工具类测试
 */
public class ExcelsTest {

    //桌面路径
    private final String path = System.getProperty("user.home") + "/Desktop/";

    @Test
    public void testTableExcelWithTemplate() {
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(Map.of("fkfs", "网上反馈", "wjmc", true, "djh", 114514, "lxdh", "联系方式1", "sfz", "身份证1", "fkr", "反馈人1", "jzks", "皮肤科"));
        data.add(Map.of("fkfs", "调查反馈", "wjmc", false, "djh", 2333, "lxdh", "联系方式2", "sfz", "身份证2", "fkr", "反馈人2", "jzks", "精神科"));
        data.add(Map.of("fkfs", "233反馈", "wjmc", true, "djh", 4234, "lxdh", "联系方式3", "sfz", "身份证3", "fkr", "反馈人3", "jzks", "眼科"));
        data.add(Map.of("fkfs", "强迫反馈", "wjmc", true, "djh", 4444, "lxdh", "联系方式4", "sfz", "身份证4", "fkr", "反馈人4", "jzks", "肛肠科"));
        //将数据写到桌面的文件中
        TemplateTableExcelWriter.builder()
                .from("template\\投诉管理导出模板.xlsx")
                .with(data)
                .to(path + "testTableExcelWithTemplate.xlsx")
                .doWrite();
    }

    @Test
    public void testExcels1() throws Exception {
        long start = System.currentTimeMillis();
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Person p = new Person().setFkfs(false).setName("张三").setWjmc("问卷" + i)
                    .setDjh(i + 101).setLxdh("fjwofeof").setSfz("fsjojweo" + i)
                    .setFkr("张三").setJzks("皮肤科").setDate(new Date());
            list.add(p);
        }
        Excels.write("template\\投诉管理导出模板.xlsx", list, path + "testExcels1.xlsx");
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testExcels2() {
        Map<String, Object> map = Map.of("name", "狗剩", "age", 22, "test1", "嘻嘻嘻嘻", "test2", "呵呵呵", "test3", "哈哈哈");
        TemplateFormExcelWriter.builder()
                .from("template\\投诉管理导出模板.xls")
                .with(map)
                .to(path + "testExcels2.xlsx")
                .doWrite();
    }

    @Test
    public void testExcels3() {
        List<Map<String, Object>> map = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Student student = new Student().setIndex(i + 1).setName("张三" + i)
                    .setAge(i * 10).setBirthday(new Date())
                    .setFkfs(i % 2 == 0)
                    .setRemark("你是一个一个一个备注啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
            map.add(Beans.toMap(student));
        }
        TableExcelWriter.builder()
                .fileType(FileType.XLSX)
                .with(map, Student.class)
                .to(path + "testExcels3.xlsx")
                .title("测试生成表格")
                .doWrite();

    }

    @Test
    public void testExcels4() {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Student student = new Student().setIndex(i + 1).setName("里斯" + i)
                    .setAge(i * 10).setBirthday(new Date())
                    .setFkfs(i % 2 == 0)
                    .setRemark("你是一个一个一个备注啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
            list.add(student);
        }
        Excels.write(list, Student.class, path + "testExcels4.xlsx");
    }
}
