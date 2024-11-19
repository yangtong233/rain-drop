package org.raindrop.common.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * created by yangtong on 2024/6/4 20:59:42
 * poi的读取操作
 */
public class ExcelReadTest {
    private final String path = System.getProperty("user.home") + "/Desktop/";

    //测试前，先创建excel文件
    @Before
    public void before() {
        new ExcelWriteTest().bigDataWriteExcel();
    }

    /**
     * 读取excel
     * 同excel的写入api，XSSFWorkbook的不同实现类的api是一样的
     */
    @Test
    public void readExcel() {
        //1.通过流创建工作簿
        try (
                FileInputStream fis = new FileInputStream(path + "bigdata-poi.xlsx");
                //读取07版本的excel
                XSSFWorkbook workbook = new XSSFWorkbook(fis)
        ) {
            //2.除了通过名称，也可以通过下标获取工作表
            Sheet sheet = workbook.getSheetAt(0);

            //3.获取行
            Row row = sheet.getRow(0);

            //4.获取单元格
            Cell cell = row.getCell(0);

            //5.根据单元格内容的不同类型调用不同api读取不同类型的数据
            Object value = Cells.parseValue(cell);
            System.out.println("第一行第一列数据为:" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取工作簿的元数据，比如：工作表个数、工作表行数，每行的列数
     */
    @Test
    public void metaData() {
        try (
                FileInputStream fis = new FileInputStream(path + "bigdata-poi.xlsx");
                //读取07版本的excel
                XSSFWorkbook workbook = new XSSFWorkbook(fis)
        ) {
            //2.除了通过名称，也可以通过下标获取工作表
            Sheet sheet = workbook.getSheetAt(0);

            //3.获取行
            Row row = sheet.getRow(0);

            //4.获取工作表的个数
            int numberOfSheets = workbook.getNumberOfSheets();
            System.out.println(numberOfSheets);

            //5.得到工资表的行数
            int lastRowNum = sheet.getLastRowNum();
            //由于lastRowNum是最后一行的下标，所以为了得到行数要+1
            System.out.println(lastRowNum + 1);
            //也可以直接获取行数，打印65536
            System.out.println(sheet.getPhysicalNumberOfRows());

            //6.获取当前行的单元格数量，打印20
            if (row != null) {
                int cellNum = row.getPhysicalNumberOfCells();
                System.out.println(cellNum);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试后，删除excel文件
    @After
    public void after() throws IOException {
        Files.delete(Path.of(path + "bigdata-poi.xlsx"));
    }
}
