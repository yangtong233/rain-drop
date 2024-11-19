package org.raindrop.common.utils.excel.support;

import cn.hutool.core.io.IoUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.raindrop.common.enums.Placeholder;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.excel.Cells;
import org.raindrop.common.utils.excel.ExcelCell;
import org.raindrop.common.utils.excel.Excels;
import org.raindrop.common.utils.excel.IExcelWriter;
import org.raindrop.common.utils.string.Strs;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于模板的表格型excel创建类
 */
public class TemplateTableExcelWriter implements IExcelWriter {

    /**
     * 模板文件输入流
     */
    private InputStream templateStream;
    /**
     * 填充的数据
     */
    private List<Map<String, Object>> data;
    /**
     * 目标文件输出流
     */
    private OutputStream targetStream;

    private TemplateTableExcelWriter() {
    }

    //开始构建
    public static TemplateTableExcelWriter builder() {
        return new TemplateTableExcelWriter();
    }

    /**
     * 指定模板路径
     *
     * @param classPath 类路径
     * @return 当前对象
     */
    @Override
    public TemplateTableExcelWriter from(String classPath) {
        //获取文件流
        templateStream = this.getClass().getClassLoader().getResourceAsStream(classPath);
        return this;
    }

    /**
     * 指定填充数据
     *
     * @param data map表示每一行，map的键值对表示该行的列
     * @return 当前对象
     */
    public TemplateTableExcelWriter with(List<Map<String, Object>> data) {
        this.data = data;
        return this;
    }

    /**
     * 写入的目标文件的路径
     *
     * @param targetPath 目标文件的路径
     * @return 当前对象
     */
    public TemplateTableExcelWriter to(String targetPath) {
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

    /**
     * 写入的目标文件
     *
     * @param targetFile 目标文件
     */
    public TemplateTableExcelWriter to(File targetFile) {
        if (targetFile == null) {
            throw new BaseException("目标文件不能为空");
        }
        try {
            to(new BufferedOutputStream(new FileOutputStream(targetFile)));
            return this;
        } catch (FileNotFoundException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 写入的目标文件的输出流
     *
     * @param targetStream 输出流
     */
    @Override
    public TemplateTableExcelWriter to(OutputStream targetStream) {
        if (targetStream == null) {
            throw new BaseException("目标文件输出流不能为空");
        }
        this.targetStream = targetStream;
        return this;
    }

    /**
     * 进行填充
     */
    @Override
    public void doWrite() {
        //校验是否为空
        if (templateStream == null || data == null || targetStream == null) {
            throw new BaseException("尚未设置模板文件路径、填充数据或者写入的目标文件");
        }
        //获取Workbook
        try (Workbook workbook = Excels.generateWorkbook(templateStream)) {
            //使用第一个工作表
            Sheet sheet = workbook.getSheetAt(0);

            //得到当前模板的最后一行下标，并基于此行填充数据
            int lastRowNum = sheet.getLastRowNum();
            //解析最后一行的所有列
            Map<Integer, ExcelCell> cells = parseRow(sheet.getRow(lastRowNum));
            //移除占位符所在行，并正式写入数据
            sheet.shiftRows(lastRowNum + 1, lastRowNum + 1, -1);

            //逐行添加数据
            for (int i = lastRowNum; i < lastRowNum + data.size(); i++) {
                //创建行
                Row row = sheet.createRow(i);
                //从数据源得到当前行的数据
                Map<String, Object> dataMap = data.get(i - lastRowNum);

                //遍历每一列，填充数据
                for (Integer index : cells.keySet()) {
                    ExcelCell currentCell = cells.get(index);
                    //单元格的真实内容和样式
                    ExcelCell insertedCell = new ExcelCell()
                            .setCellStyle(currentCell.getCellStyle())
                            .setContent(Strs.parseByPlaceholder(
                                    //currentCell.getContent()肯定不为空
                                    currentCell.getContent().toString(),
                                    dataMap,
                                    currentCell.getPrefix(),
                                    currentCell.getSuffix()
                            ));
                    //创建单元格，并写入内容
                    Cell cell = row.createCell(index);
                    insertedCell.setWorkbook(workbook);
                    Cells.writeCellValue(cell, insertedCell);
                }
            }

            //将工作簿写入目标文件
            workbook.write(targetStream);
        } catch (Exception e) {
            throw new BaseException(e);
        } finally {
            //关闭输出流
            IoUtil.close(targetStream);
        }
    }

    /**
     * 解析指定行，并建立列下标与ExcelCell的映射
     *
     * @param row 当前行对象
     * @return Map<列下标, ExcelCell对象>
     */
    public Map<Integer, ExcelCell> parseRow(Row row) {
        //得到改行最后一列下标
        short lastCellNum = row.getLastCellNum();

        //单元格下标与通配符的对应关系，存放在该map里
        Map<Integer, ExcelCell> map = new HashMap<>();

        //遍历该行的所有列
        for (int i = 0; i < lastCellNum; i++) {
            //得到当前单元格
            Cell cell = row.getCell(i);

            Object cellValue = Cells.parseValue(cell);
            if (cellValue != null) {
                //根据当前单元格，构造ExcelCell对象
                ExcelCell excelCell = new ExcelCell()
                        .setCellStyle(cell.getCellStyle())
                        .setPrefix(Placeholder.LEFT_BRACKET)
                        .setSuffix(Placeholder.RIGHT_BRACKET)
                        .setContent(cellValue);

                //建立列下标与ExcelCell对象的映射关系
                map.put(i, excelCell);
            }
        }

        return map;
    }


}
