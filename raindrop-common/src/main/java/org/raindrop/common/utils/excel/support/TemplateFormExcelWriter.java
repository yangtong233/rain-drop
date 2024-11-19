package org.raindrop.common.utils.excel.support;

import cn.hutool.core.io.IoUtil;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.raindrop.common.enums.Placeholder;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.excel.Cells;
import org.raindrop.common.utils.excel.Excels;
import org.raindrop.common.utils.excel.IExcelWriter;
import org.raindrop.common.utils.string.Strs;

import java.io.*;
import java.util.Map;

/**
 * 基于模板的表单型excel创建类
 */
public class TemplateFormExcelWriter implements IExcelWriter {
    /**
     * 模板文件
     */
    private InputStream templateStream;
    /**
     * 填充到模板的业务数据
     */
    private Map<String, Object> dataSource;
    /**
     * 目标文件输出流
     */
    private OutputStream targetStream;

    private TemplateFormExcelWriter() {
    }

    //开始创建
    public static TemplateFormExcelWriter builder() {
        return new TemplateFormExcelWriter();
    }

    /**
     * 指定模板文件的类路径
     *
     * @param classPath 模板文件路径
     * @return this
     */
    @Override
    public TemplateFormExcelWriter from(@NotNull String classPath) {
        //获取模板文件的文件流
        templateStream = this.getClass().getClassLoader().getResourceAsStream(classPath);
        return this;
    }

    /**
     * 指定要填充到模板的数据
     *
     * @param data 数据集合, k-模板文件的占位符，v-要填充的值
     * @return this
     */
    public TemplateFormExcelWriter with(Map<String, Object> data) {
        this.dataSource = data;
        return this;
    }

    /**
     * 写入的目标文件的路径
     *
     * @param targetPath 目标文件的路径
     * @return 当前对象
     */
    public TemplateFormExcelWriter to(String targetPath) {
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
    public TemplateFormExcelWriter to(File targetFile) {
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
    public TemplateFormExcelWriter to(OutputStream targetStream) {
        if (targetStream == null) {
            throw new BaseException("目标文件的输出流不能为空");
        }
        this.targetStream = targetStream;
        return this;
    }

    /**
     * 模板文件 + 数据 = 目标excel文件
     */
    @Override
    public void doWrite() {
        if (templateStream == null || dataSource == null || targetStream == null) {
            throw new BaseException("必须同时指定正确的模板路径、填充数据和输出文件");
        }
        try (Workbook workbook = Excels.generateWorkbook(templateStream)) {
            //获取指定下标的sheet
            Sheet sheet = workbook.getSheetAt(0);

            //可以获取当前sheet最后一行的下标
            int lastRowNum = sheet.getLastRowNum();
            //从第一行开始遍历
            for (int i = 0; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                //遇到空行则停止
                if (row == null) {
                    continue;
                }
                //获取当前行的总列数
                short lastCellNum = row.getLastCellNum();
                //从该行第一列开始遍历
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        //读取该单元格内容
                        String content = String.valueOf(Cells.parseValue(cell));
                        //解析单元格内容
                        content = Strs.parseByPlaceholder(
                                content,
                                dataSource,
                                Placeholder.LEFT_CURLY_BRACE_PLUS,
                                Placeholder.RIGHT_CURLY_BRACE
                        );
                        //写入单元格内容
                        cell.setCellValue(content);
                    }
                }
            }
            workbook.write(targetStream);
        } catch (Exception e) {
            throw new BaseException(e);
        } finally {
            IoUtil.close(targetStream);
        }
    }

}
