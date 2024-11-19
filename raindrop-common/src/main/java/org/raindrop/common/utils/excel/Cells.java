package org.raindrop.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.utils.string.Strs;
import org.springframework.lang.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by yangtong on 2024/6/4 22:04:59
 * excel单元格工具类
 */
public class Cells {
    /**
     * 根据目标单元格类型，读取该单元格的值
     *
     * @param cell 目标单元格
     * @return 单元格的值
     */
    public static Object parseValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        Object value = null;
        switch (cell.getCellType()) {
            //字符串类型
            case CellType.STRING -> value = cell.getStringCellValue();
            //数字类型，整型、小数、日期
            case CellType.NUMERIC -> {
                //由于日期也是数字类型，所以要对日期进行单独判断
                if (DateUtil.isCellDateFormatted(cell)) {
                    //是日期
                    value = cell.getDateCellValue();
                } else {
                    //不是日期
                    value = cell.getNumericCellValue();
                }
            }
            //布尔类型
            case CellType.BOOLEAN -> value = cell.getBooleanCellValue();
            //公式类型
            case CellType.FORMULA -> value = cell.getCellFormula();
            //空字符串
            case CellType.BLANK -> value = "";
            //未知类型
            case CellType._NONE -> value = "未知类型";
            //发生错误
            case CellType.ERROR -> value = cell.getErrorCellValue();
        }
        return value;
    }

    /**
     * 向单元格写入内容
     * @param excelCell 写入的内容
     */
    public static void writeCellValue(@NonNull ExcelCell excelCell) {
        if (excelCell.getCell() == null) {
            throw new BaseException("必须给ExcelCell设置单元格cell属性");
        }
        writeCellValue(excelCell.getCell(), excelCell);
    }

    /**
     * 向单元格写入内容
     *
     * @param cell      单元格
     * @param excelCell 写入的内容
     */
    public static void writeCellValue(@NonNull Cell cell, @NonNull ExcelCell excelCell) {
        //写入样式
        if (excelCell.getCellStyle() != null) {
            //自动换行
            excelCell.getCellStyle().setWrapText(true);
            cell.setCellStyle(excelCell.getCellStyle());
        }

        //单元格内容
        Object content = excelCell.getContent();
        Object _content;

        //如果是数字格式
        if ((_content = Strs.tryToLong(content.toString())) != null) {
            cell.setCellValue((Long) _content);
        }
        //如果是浮点格式
        else if ((_content = Strs.tryToFloat(content.toString())) != null) {
            cell.setCellValue((Double) _content);
        }
        //如果是布尔格式
        else if ((_content = Strs.tryToBool(content.toString())) != null) {
            cell.setCellValue((Boolean) _content);
        }
        //如果是日期格式
        else if ((_content = Strs.tryToDate(content.toString())) != null) {
            cell.setCellValue(new SimpleDateFormat("yyyy/M/d").format((Date) _content));
        }
        //否则统一当字符串处理
        else {
            cell.setCellValue(content.toString());
        }
    }

    /**
     * 向单元格设置边框样式
     *
     * @param cell        样式对象
     * @param borderStyles 边框样式
     * @return 设置了边框后的样式
     */
    public static CellStyle setBorderStyle(ExcelCell cell, BorderStyle... borderStyles) {
        CellStyle style = cell.getCellStyle();
        //设置边框
        if (borderStyles.length == 0) {
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        } else if (borderStyles.length == 1) {
            style.setBorderTop(borderStyles[0]);
            style.setBorderBottom(borderStyles[0]);
            style.setBorderLeft(borderStyles[0]);
            style.setBorderRight(borderStyles[0]);
        } else if (borderStyles.length == 2) {
            style.setBorderTop(borderStyles[0]);
            style.setBorderBottom(borderStyles[0]);
            style.setBorderLeft(borderStyles[1]);
            style.setBorderRight(borderStyles[1]);
        } else if (borderStyles.length == 3) {
            style.setBorderTop(borderStyles[0]);
            style.setBorderBottom(borderStyles[0]);
            style.setBorderLeft(borderStyles[1]);
            style.setBorderRight(borderStyles[2]);
        } else {
            style.setBorderTop(borderStyles[0]);
            style.setBorderRight(borderStyles[1]);
            style.setBorderBottom(borderStyles[2]);
            style.setBorderLeft(borderStyles[3]);
        }
        return style;

    }

    /**
     * 设置居中对齐样式
     * @param cell 单元格对象
     * @return 设置了居中对齐后的样式
     */
    public static CellStyle setCenterAlignment(ExcelCell cell) {
        return setAlignment(cell, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
    }

    /**
     * 设置对齐样式
     * @param cell 单元格
     * @param horizontal 水平对齐
     * @param vertical 垂直对齐
     * @return 设置了对齐后的样式
     */
    public static CellStyle setAlignment(ExcelCell cell, HorizontalAlignment horizontal, VerticalAlignment vertical) {
        CellStyle style = cell.getCellStyle();
        style.setAlignment(horizontal);    //水平
        style.setVerticalAlignment(vertical);  //垂直
        return style;
    }

    public static CellStyle setFont(ExcelCell cell, String fontName, short fontSize, boolean isBold, boolean isItalic) {
        CellStyle style = cell.getCellStyle();

        Font font = cell.getWorkbook().createFont();
        //宋体18号加粗
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(isBold);
        font.setItalic(isItalic);
        style.setFont(font);
        return style;
    }

    public static CellStyle setBackgroundColor(ExcelCell cell, int r, int g, int b) {
        Workbook workbook = cell.getWorkbook();
        Color backgroundColor;
        if (workbook instanceof HSSFWorkbook) {
             backgroundColor = new HSSFColor(0x40, -1, new java.awt.Color(r, g, b));
        } else {
            backgroundColor = new XSSFColor(new java.awt.Color(r, g, b), null);
        }
        CellStyle style = cell.getCellStyle();
        style.setFillForegroundColor(backgroundColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }
}
