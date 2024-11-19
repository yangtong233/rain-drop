package org.raindrop.common.utils.excel.support;

import cn.hutool.core.io.IoUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.raindrop.common.annos.Marker;
import org.raindrop.common.enums.FileType;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.excel.Cells;
import org.raindrop.common.utils.excel.ExcelCell;
import org.raindrop.common.utils.excel.IExcelWriter;
import org.springframework.lang.NonNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 直接基于数据的表格型excel创建类
 */
public class TableExcelWriter implements IExcelWriter {

    /**
     * 模板文件输入流
     */
    private List<Map<String, Object>> data;
    /**
     * 目标文件输出流
     */
    private OutputStream targetStream;
    /**
     * excel文件类型
     */
    private FileType fileType;
    /**
     * 标题
     */
    private String title;
    /**
     * 表头
     */
    private LinkedHashMap<String, Marker> header;

    public static TableExcelWriter builder() {
        return new TableExcelWriter();
    }

    @Override
    public TableExcelWriter from(String classPath) {
        throw new BaseException("直接生成excel文件，无需模板文件");
    }

    /**
     * 指定填充数据
     *
     * @param data  map表示每一行，map的键值对表示改行的列
     * @param clazz 数据类型，以此来产生表头
     * @return 当前对象
     */
    public <T> TableExcelWriter with(@NonNull List<Map<String, Object>> data, @NonNull Class<T> clazz) {
        this.data = data;
        //根据clazz解析出表头
        Field[] candidateField = clazz.getDeclaredFields();
        this.header = new LinkedHashMap<>(candidateField.length);
        Arrays.stream(candidateField).filter(field -> {
            field.setAccessible(true);
            return field.isAnnotationPresent(Marker.class);
        }).forEach(field -> header.put(field.getName(), field.getAnnotation(Marker.class)));

        return this;
    }

    /**
     * 指定填充数据到目标路径
     *
     * @param targetPath 路径
     * @return this
     */
    public TableExcelWriter to(String targetPath) {
        if (targetPath == null) {
            throw new BaseException("目标文件路径不能为空");
        }
        try {
            to(new BufferedOutputStream(new FileOutputStream(targetPath)));
            return this;
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        }
    }

    @Override
    public TableExcelWriter to(OutputStream targetStream) {
        if (targetStream == null) {
            throw new BaseException("目标文件输出流不能为空");
        }
        this.targetStream = targetStream;
        return this;
    }

    /**
     * 指定文件类型
     *
     * @param fileType excel文件类型
     * @return this
     */
    public TableExcelWriter fileType(FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public TableExcelWriter title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void doWrite() {
        if (data == null || targetStream == null) {
            throw new BaseException("填充数据或目标输出流不能为空");
        }
        try (
                Workbook workbook = FileType.XLS
                        .equals(fileType) ?
                        new HSSFWorkbook() : new XSSFWorkbook()
        ) {
            Sheet sheet = workbook.createSheet(title);
            //创建表头行
            Row row = sheet.createRow(0);
            //冻结表头行
            sheet.createFreezePane(0, 1);
            row.setHeightInPoints(22);
            int index = 0;
            for (String title : header.keySet()) {
                Marker marker = header.get(title);
                Cell cell = row.createCell(index);
                //内容和样式
                ExcelCell headerCell = new ExcelCell()
                        .setContent(marker.name())
                        .setWorkbook(workbook)
                        .setCellStyle(workbook.createCellStyle());

                Cells.setCenterAlignment(headerCell);
                Cells.setBorderStyle(headerCell, BorderStyle.NONE);
                Cells.setFont(headerCell, "宋体", (short) 12, false, false);
                Cells.setBackgroundColor(headerCell, 242, 242, 242);
                Cells.writeCellValue(cell, headerCell);

                //列宽
                sheet.setColumnWidth(index, marker.width() * 256);

                //列下标自增1
                index++;
            }

            //写入表格主体数据
            for (int i = 1; i <= data.size(); i++) {
                Map<String, Object> rowData = data.get(i - 1);
                Row dataRow = sheet.createRow(i);
                //行高
                dataRow.setHeightInPoints(17);
                int j = 0;
                for (String title : header.keySet()) {
                    //创建单元格
                    Cell cell = dataRow.createCell(j);
                    ExcelCell insertedCell = new ExcelCell()
                            .setCell(cell)
                            .setContent(rowData.get(title).toString())
                            .setWorkbook(workbook)
                            .setCellStyle(workbook.createCellStyle());

                    Cells.setCenterAlignment(insertedCell);
                    Cells.writeCellValue(insertedCell);

                    //列下标自增1
                    j++;
                }
            }

            workbook.write(targetStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(targetStream);
        }

    }

}
