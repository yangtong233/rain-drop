package org.raindrop.common.utils.excel.support;

import cn.hutool.core.io.IoUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.excel.Cells;
import org.raindrop.common.utils.excel.ExcelCell;
import org.raindrop.common.utils.excel.Excels;
import org.raindrop.common.utils.excel.IExcelWriter;

import java.io.*;
import java.util.List;

/**
 * 基于模板(不带占位符)的表格型excel创建类
 * 从指定行指定列开始，按照顺序往模板写入数据
 */
public class TemplateTableExcelNonPlaceholderWriter implements IExcelWriter {

    /**
     * 模板文件输入流
     */
    private InputStream templateStream;
    /**
     * 填充的数据
     */
    private List<List<Object>> data;
    /**
     * 目标文件输出流
     */
    private OutputStream targetStream;
    /**
     * 从哪一行开始写入
     */
    private int startRow;
    /**
     * 从哪一列开始写入
     */
    private int startCol;

    private TemplateTableExcelNonPlaceholderWriter() {
    }

    //开始构建
    public static TemplateTableExcelNonPlaceholderWriter builder() {
        return new TemplateTableExcelNonPlaceholderWriter();
    }

    /**
     * 指定模板路径
     *
     * @param classPath 类路径
     * @return 当前对象
     */
    @Override
    public TemplateTableExcelNonPlaceholderWriter from(String classPath) {
        //获取文件流
        templateStream = this.getClass().getClassLoader().getResourceAsStream(classPath);
        return this;
    }

    /**
     * 指定填充数据
     *
     * @param data map表示每一行，map的键值对表示该行的列，LinkedHashMap保证元素有序
     * @return 当前对象
     */
    public TemplateTableExcelNonPlaceholderWriter with(List<List<Object>> data) {
        this.data = data;
        return this;
    }

    public TemplateTableExcelNonPlaceholderWriter startRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public TemplateTableExcelNonPlaceholderWriter startCol(int startCol) {
        this.startCol = startCol;
        return this;
    }


    /**
     * 写入的目标文件的路径
     *
     * @param targetPath 目标文件的路径
     * @return 当前对象
     */
    public TemplateTableExcelNonPlaceholderWriter to(String targetPath) {
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
    public TemplateTableExcelNonPlaceholderWriter to(File targetFile) {
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
    public TemplateTableExcelNonPlaceholderWriter to(OutputStream targetStream) {
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

            //逐行添加数据
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + startRow);
                List<Object> list = data.get(i);
                //逐列写入数据
                for (int j = 0; j < list.size(); j++) {
                    Cell currentCell = row.createCell(j + startCol);
                    ExcelCell insertedCell = new ExcelCell()
                            .setCellStyle(currentCell.getCellStyle())
                            .setContent(list.get(j));
                    Cells.setBorderStyle(insertedCell);

                    insertedCell.setWorkbook(workbook);
                    Cells.writeCellValue(currentCell, insertedCell);
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

}
