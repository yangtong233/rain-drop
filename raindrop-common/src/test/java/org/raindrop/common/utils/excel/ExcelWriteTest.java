package org.raindrop.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * created by yangtong on 2024/6/4 19:53:59
 * poi的写入操作
 */
public class ExcelWriteTest {

    //桌面路径
    private final String path = System.getProperty("user.home") + "/Desktop/";

    /**
     * 使用HSSFWorkbook向03版本的excel写入数据
     * 在桌面创建一个xls文件，并创建一个sheet，向该sheet的第一行前20列写入数据
     * 操作07版本的excel，可以无缝切换成XSSFWorkbook，api都是一样的
     * 区别：
     * 03 最多支持65536行，将数据放入缓存，然后一次性写入磁盘，比较快
     * 07 可以支持几百万行，所以不可能一次性写入磁盘，而是一部分一部分的写，所以速度会慢一点，而且每次都会获取全部行
     */
    @Test
    public void writeExcel() {
        //1.创建工作簿
        try (
                //操作07版本的excel时，只需要将Workbook实现类换成XSSFWorkbook
                Workbook workbook = new HSSFWorkbook();
                OutputStream os = new FileOutputStream(path + "hello-poi.xls");
        ) {
            //2.创建工作表，通过名称创建
            Sheet sheet = workbook.createSheet("03版本excel写入测试");
            //3.创建行（第一行，列下标从0开始）
            Row row1 = sheet.createRow(0);

            //向前20列写入数据
            for (int i = 0; i < 20; i++) {
                //4.创建单元格（列下标从0开始）
                Cell cell = row1.createCell(i);
                //5.向当前单元格写入数据，写入字符串，还可以写入数字、日期等
                cell.setCellValue("hello poi" + i);
            }

            //6.创建xls文件，并将workbook写入该文件
            workbook.write(os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 大数据量写入
     * 通过测试，写入相同的数据，当实现类是XSSFWorkbook时，速度明显变慢
     * 写入大数据量时，需要使用poi的SXSSF类
     */
    @Test
    public void batchWriteExcel() {
        //开始时间
        long startTime = System.currentTimeMillis();
        try (
                Workbook workbook = new HSSFWorkbook();
                //Workbook workbook = new XSSFWorkbook();
                OutputStream os = new FileOutputStream(path + "batch-poi.xlsx");
        ) {
            Sheet sheet = workbook.createSheet("batch");
            //写入03版本excel的最大行数
            for (int rowNum = 0; rowNum < 65536; rowNum++) {
                Row row = sheet.createRow(rowNum);
                for (int cellNum = 0; cellNum < 20; cellNum++) {
                    Cell cell = row.createCell(cellNum);
                    cell.setCellValue("batch write:" + rowNum + ", " + cellNum);
                }
            }
            //生成表
            workbook.write(os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //结束
            long end = System.currentTimeMillis();
            //本机耗时:2564ms(HSSFWorkbook)
            //本机耗时:15619ms(XSSFWorkbook)
            System.out.println("共耗时：" + (end - startTime));
        }
    }

    /**
     * 使用SXSSF测试大数据写入，不会内存溢出，而是速度很快
     * 不会获取全部行，而是获取"滑动窗口"范围内的行，窗口内的数据存在于内存中，当超出这个窗口时，行数最小的数据会被刷入磁盘
     * 窗口默认大小为100
     * SXSSF会用到临时文件，这些文件需要我们用dispose方法手动清除
     * SXSSF的意思"super XSSF"，XSSF升级版，api和HSSF、XSSF一样，所以直接切换实现类即可
     * HSSF没有super版，因为最多也才65536行，直接读写就是了
     */
    @Test
    public void bigDataWriteExcel() {
        //开始时间
        long startTime = System.currentTimeMillis();
        try (
                //构造参数：滑动窗口大小
                SXSSFWorkbook workbook = new SXSSFWorkbook(100);
                OutputStream os = new FileOutputStream(path + "bigdata-poi.xlsx");
        ) {
            Sheet sheet = workbook.createSheet("big data");
            //写入03版本excel的最大行数
            for (int rowNum = 0; rowNum < 65536; rowNum++) {
                Row row = sheet.createRow(rowNum);
                for (int cellNum = 0; cellNum < 20; cellNum++) {
                    Cell cell = row.createCell(cellNum);
                    cell.setCellValue("batch write:" + rowNum + ", " + cellNum);
                }
            }
            //生成表
            workbook.write(os);
            //清除临时文件
            workbook.dispose();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //结束
            long end = System.currentTimeMillis();
            //本机耗时:2477ms，比上面写入HSSF还快
            System.out.println("共耗时：" + (end - startTime));
        }
    }

    /**
     * 单元格样式
     * 1.列宽
     * 2.行高
     * 3.边框线
     * 4.合并单元格
     * 5.字体
     * 6.对齐方式
     */
    @Test
    public void writeExcelWithStyle() throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("样式sheet");


        //1.标题行(边框线，行高，合并单元格，对齐方式，字体)
        Row titleRow = sheet.createRow(0);
        //1.1 创建样式对象,设置边框属性，上下左右边框设为细线
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        //1.2对齐方式 水平对齐，垂直对齐
        titleStyle.setAlignment(HorizontalAlignment.CENTER);    //水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中

        //1.3 字体, 黑体18号
        Font font = workbook.createFont();
        font.setFontName("黑体");     //指定的字体，必须在操作系统能找到
        font.setFontHeightInPoints((short) 18);
        titleStyle.setFont(font);

        //1.4 设置行高
        titleRow.setHeightInPoints(42);
        for (int i = 0; i < 5; i++) {
            Cell cell = titleRow.createCell(i);
            //1.5 将样式对象赋给单元格
            cell.setCellStyle(titleStyle);
            //1.6 设置列宽15个字符长度，参数：列下标, 宽度(单位:一个标准字母宽度的1/256)
            sheet.setColumnWidth(i, 15 * 256);
        }
        //1.7 合并单元格,CellRangeAddress参数：int firstRow, int lastRow, int firstCol, int lastCol
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));    //合并第一行0~4列单元格
        //1.8 写入标题行
        titleRow.getCell(0).setCellValue("这是一个标题");

        //2. 表头行样式
        CellStyle headerStyle = workbook.createCellStyle();
        //2.1 对于边框和对齐方式，与titleRow的样式一样，所以可以直接克隆上面的
        headerStyle.cloneStyleFrom(titleStyle);
        //2.2 单独设置字体，覆盖掉克隆过来的
        Font headerFont = workbook.createFont();
        headerFont.setFontName("宋体");
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);   //设置粗体
        headerStyle.setFont(headerFont);
        //2.3 创建表头行，设置行高
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(31.5F);
        //2.4 创建该行的单元格，设置内容和样式
        String[] header = new String[]{"序号", "姓名", "年龄", "性别", "爱好"};
        for (int i = 0; i < 5; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header[i]);
            //列宽已经设置了，不需在设置
            //sheet.setColumnWidth(i, 15 * 256);
        }

        //3. 内容的样式
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.cloneStyleFrom(headerStyle);
        Font contentFont = workbook.createFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 11);
        contentFont.setBold(false);   //设置粗体
        contentStyle.setFont(contentFont);
        //循环创建行列
        for (int i = 0; i < 10; i++) {
            //从第三行开始正式写入内容
            Row row = sheet.createRow(i + 2);
            String[] content = new String[]{"index", "张三", "22", "♂", "唱跳rap篮球"};
            row.setHeightInPoints(31);
            for (int j = 0; j < 5; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(contentStyle);
                if (j == 0) {
                    cell.setCellValue(i + 1);
                    continue;
                }
                cell.setCellValue(content[j]);
            }
        }

        //写入与关闭
        workbook.write(new FileOutputStream(path + "带样式excel.xls"));
        workbook.close();
    }

    /**
     * 复杂样式
     * 设置样式的代码是很繁琐的，对复杂的excel，可以使用模板
     * 模板里提前设置了样式，我们只需将数据往里面填充就行了(表格型模板)
     */
    @Test
    public void writeExcelWithTemplate() throws Exception {
        //1.获取模板
        Workbook workbook = new HSSFWorkbook(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("template\\投诉管理导出模板.xls")));
        Sheet sheet = workbook.getSheetAt(0);
        //2. 准备好数据
        List<String> list1 = List.of("反馈方式1", "问卷1", "114514", "fsdfsdfsd", "fsdfsdfwwef", "fwefwefew");
        List<String> list2 = List.of("反馈方式2", "发发的", "1111", "fsdfsdfsd", "fsdfsdfwwef", "fwefwefew");
        List<String> list3 = List.of("反馈方式3", "防守打法啥段位", "22323", "fsdfsdfsd", "fsdfsdfwwef", "fwefwefew");
        List<String> list4 = List.of("反馈方式4", "归属感", "22", "fsdf3333sdfsd", "fsdfsdfwwef", "fwefwefew");
        List<String> list5 = List.of("反馈方式5", "请问请问", "2222", "范文芳", "fsdfsdfwwef", "防守打法");
        List<List<String>> data = List.of(list1, list2, list3, list4, list5);
        //3.填充数据
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i < lastRowNum + data.size(); i++) {
            Row row = sheet.createRow(i);
            row.setHeightInPoints(30);
            List<String> list = data.get(i - lastRowNum);
            for (int j = 0; j < list.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(list.get(j));
            }
        }

        //写入与关闭
        workbook.write(new FileOutputStream(path + "带模板excel.xls"));
        workbook.close();
    }

    /**
     * 移动行和列
     * shiftRows(int startRow, int endRow, int n) -> 参数：要移动的起始行的索引，要移动的结束行的索引，移动的行数。正数表示向下移动，负数表示向上移动
     * shiftColumns，移动列同理
     */
    @Test
    public void moveRow() {
        try (
                Workbook workbook = new HSSFWorkbook();
                OutputStream os = new FileOutputStream(path + "move.xls");
        ) {
            Sheet sheet = workbook.createSheet("移动行测试");
            //1.创建两行
            for (int i = 0; i < 2; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue("hello world:" + i);
                row.createCell(1).setCellValue(i);
            }
            //2.将第二行（1，1）向上移动一行（-1），第一行被覆盖
            sheet.shiftRows(1, 1, -1);
            //3.将第二列（1，1）向左移动一列（-1），第一列被覆盖
            sheet.shiftColumns(1, 1, -1);

            //6.创建xls文件，并将workbook写入该文件
            workbook.write(os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
