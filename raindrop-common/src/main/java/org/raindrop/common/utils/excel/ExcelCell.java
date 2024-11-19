package org.raindrop.common.utils.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.*;
import org.raindrop.common.enums.Placeholder;

/**
 * excel的单元格对象
 */
@Data
@Accessors(chain = true)
public class ExcelCell {

    /**
     * 单元格对象
     */
    private Cell cell;
    /**
     * 单元格类型
     */
    private CellType cellType;
    /**
     * 单元格样式
     */
    private CellStyle cellStyle;

    /**
     * 单元格原始内容
     */
    private Object content;

    /**
     * 占位符前缀
     */
    private Placeholder prefix;

    /**
     * 占位符后缀
     */
    private Placeholder suffix;
    /**
     * 所属工作簿
     */
    private Workbook workbook;
    /**
     * 所属工作表
     */
    private Sheet sheet;
}
