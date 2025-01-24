package org.raindrop.common.utils.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.raindrop.common.annos.Marker;
import org.raindrop.common.enums.FileType;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.bean.Beans;
import org.raindrop.common.utils.excel.support.*;
import org.raindrop.common.utils.string.Strs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * excel工具类
 */
public class Excels {

    /**
     * 根据数据data生成excel文件，并写入指定路径
     *
     * @param data       数据
     * @param clazz      数据类型
     * @param targetPath 输出文件路径
     */
    public static void write(List<?> data, Class<?> clazz, String targetPath) {
        try {
            write(data, clazz, new BufferedOutputStream(new FileOutputStream(targetPath)));
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 根据数据data生成excel文件，并写入输出流targetStream
     *
     * @param data         数据
     * @param clazz        数据类型
     * @param targetStream 输出流
     */
    public static void write(List<?> data, Class<?> clazz, OutputStream targetStream) {
        //将实体数据转为map
        List<Map<String, Object>> list = data.stream()
                .map(datum -> Beans.toMap(datum, true))
                .toList();

        doWrite(list, clazz, targetStream);
    }

    public static void write(List<?> data, Class<?> clazz, OutputStream targetStream, String title) {
        //将实体数据转为map
        List<Map<String, Object>> list = data.stream()
                .map(datum -> Beans.toMap(datum, true))
                .toList();

        doWrite(list, clazz, targetStream, title);
    }

    /**
     * 根据数据data生成excel文件，并写入输出流targetStream
     *
     * @param data         数据
     * @param targetStream 输出流
     * @param clazz        数据类型
     */
    public static void doWrite(List<Map<String, Object>> data, Class<?> clazz, OutputStream targetStream) {

        //解析标题
        Marker titleAnno = clazz.getAnnotation(Marker.class);
        String title = titleAnno != null && Strs.isNotEmpty(titleAnno.value()) ? titleAnno.value() : "表格";
        doWrite(data, clazz, targetStream, title);
    }

    /**
     * 根据数据data生成excel文件，并指定sheet名称，然后写入输出流targetStream
     *
     * @param data         数据
     * @param clazz        数据类型
     * @param targetStream 输出流
     * @param title        sheet名称
     */
    public static void doWrite(List<Map<String, Object>> data, Class<?> clazz, OutputStream targetStream, String title) {
        TableExcelWriter.builder()
                .title(title)
                .with(data, clazz)
                .to(targetStream)
                .doWrite();
    }

    /**
     * 根据模板 + 数据，生成表单型的excel，通过占位符填充数据
     *
     * @param templatePath 模板类路径
     * @param data         数据
     * @param targetPath   输出文件路径
     */
    public static void write(String templatePath, Object data, String targetPath) {
        try {
            doWrite(
                    templatePath,
                    BeanUtil.beanToMap(data, false, true),
                    new BufferedOutputStream(new FileOutputStream(targetPath))
            );
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 根据模板 + 数据，生成表单型的excel，通过占位符填充数据
     *
     * @param templatePath 模板类路径
     * @param data         数据
     * @param os           输出流
     */
    public static void write(String templatePath, Object data, OutputStream os) {
        doWrite(
                templatePath,
                BeanUtil.beanToMap(data, false, true),
                os
        );
    }

    /**
     * 根据模板 + 数据，生成表单型的excel，通过占位符填充数据，并将生成的excel文件写入到输出流里
     *
     * @param templatePath 模板类路径
     * @param os           输出流
     * @param data         数据
     */
    public static void doWrite(String templatePath, Map<String, Object> data, OutputStream os) {
        TemplateFormExcelWriter.builder()
                .from(templatePath)
                .with(data)
                .to(os)
                .doWrite();
    }

    /**
     * 根据模板 + 数据生成excel文件后，将生成excel文件写入到指定路径
     *
     * @param templatePath 模板文件路径
     * @param outPath      输出文件的路径
     * @param data         数据
     */
    public static void write(String templatePath, List<?> data, String outPath) {
        try {
            write(templatePath, data, new BufferedOutputStream(new FileOutputStream(outPath)));
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 根据模板 + 数据生成excel文件后，将生成excel文件写入到指定文件
     *
     * @param templatePath 模板文件路径
     * @param data         要被填充的数据
     * @param outFile      输出文件
     */
    public static void write(String templatePath, List<?> data, File outFile) {
        try {
            write(templatePath, data, new BufferedOutputStream(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 根据模板 + 数据生成excel文件后，将生成excel文件写入到输出流os中
     *
     * @param templatePath 模板文件路径
     * @param os           输出流
     * @param data         数据
     */
    public static void write(String templatePath, List<?> data, OutputStream os) {
        //将实体数据转为map
        List<Map<String, Object>> list = data.stream()
                .map(datum -> Beans.toMap(datum, true))
                .toList();

        //使用得到的数据源map，将其填充到模板
        doWrite(templatePath, list, os);
    }

    /**
     * 根据模板 + 数据生成excel文件后，将生成excel文件写入到输出流os中
     *
     * @param templatePath 模板文件路径
     * @param os           输出流
     * @param data         数据
     */
    public static void doWrite(String templatePath, List<Map<String, Object>> data, OutputStream os) {
        TemplateTableExcelPlaceholderWriter.builder().from(templatePath).with(data).to(os).doWrite();
    }

    /**
     * 从指定行指定列向指定模板写入数据
     *
     * @param templatePath 模板文件路径
     * @param startRow     从模板第几行写入
     * @param startCol     从模板第几列写入
     * @param data         写入的数据
     * @param os           输出流
     */
    public static void doWrite(String templatePath, Integer startRow, Integer startCol, List<List<Object>> data, OutputStream os) {
        TemplateTableExcelNonPlaceholderWriter.builder()
                .from(templatePath)
                .startRow(startRow)
                .startCol(startCol)
                .with(data)
                .to(os)
                .doWrite();
    }

    /**
     * 读取Excel并转为数组
     * 从Excel的第零行第零列开始读，将每一行的单元格以此顺序赋值给target类型的字段，期间会发生类型转换
     *
     * @param source Excel文件流
     * @param target 每一行转为指定类型
     * @param <T>    类型参数
     * @return 数组
     */
    public static <T> List<T> read(InputStream source, Class<T> target) {
        List<T> result = new ArrayList<>();
        TableExcelReader.<T>builder().from(new BufferedInputStream(source)).to(target, result).doRead();
        return result;
    }

    /**
     * 读取Excel并转为数组
     * 从Excel的指定行指定列开始读，将每一行的单元格以此顺序赋值给target类型的字段，期间会发生类型转换
     *
     * @param source   Excel文件流
     * @param startRow 起始行
     * @param startCol 起始列
     * @param target   每一行转为指定类型
     * @param <T>      类型参数
     * @return 数组
     */
    public static <T> List<T> read(InputStream source, int startRow, int startCol, Class<T> target) {
        List<T> result = new ArrayList<>();
        TableExcelReader.<T>builder().from(new BufferedInputStream(source)).to(target, result).doRead(startRow, startCol);
        return result;
    }

    /**
     * 根据流产生工作簿对象，会自动根据源文件的类型自动产生03或07版本的Workbook实现类
     * 关闭InputStream输入流
     *
     * @return Workbook对象
     */
    public static Workbook generateWorkbook(InputStream is) {

        Workbook workbook;
        try {
            //根据流获取文件类型，不会关闭流
            if (!(is instanceof BufferedInputStream)) {
                is = new BufferedInputStream(is);
            }
            FileType fileType = FileType.getFileType(is);
            //根据流推断文件类型，并创建不同的Workbook实现类
            if (fileType.equals(FileType.XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (fileType.equals(FileType.XLSX)) {
                workbook = new XSSFWorkbook(is);
            } else {
                throw new BaseException("excel文件必须是xls或者xlsx类型");
            }
        } catch (Exception e) {
            throw new BaseException(e);
        } finally {
            //关闭excel文件流流
            IoUtil.close(is);
        }
        return workbook;
    }

}
