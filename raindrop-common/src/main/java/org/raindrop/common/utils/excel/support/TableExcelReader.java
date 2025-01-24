package org.raindrop.common.utils.excel.support;

import cn.hutool.core.io.IoUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.raindrop.common.utils.convert.Converts;
import org.raindrop.common.utils.excel.Cells;
import org.raindrop.common.utils.excel.Empty;
import org.raindrop.common.utils.excel.Excels;
import org.raindrop.common.utils.excel.IExcelReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * created by yangtong on 2025/1/24 14:12:49
 */
public class TableExcelReader<T> implements IExcelReader<T> {

    private InputStream source;

    private Class<T> target;

    private List<T> list;

    private TableExcelReader() {
    }

    public static <R> TableExcelReader<R> builder() {
        return new TableExcelReader<R>();
    }

    @Override
    public IExcelReader<T> from(InputStream source) {
        this.source = source;
        return this;
    }

    @Override
    public IExcelReader<T> to(Class<T> target, List<T> result) {
        this.target = target;
        this.list = result;
        return this;
    }

    @Override
    public void doRead() {
        doRead(0, 0);
    }

    @Override
    public void doRead(int startRow, int startCol) {
        try (Workbook workbook = Excels.generateWorkbook(source)) {
            Sheet sheet = workbook.getSheetAt(0);
            //最后一行下标
            int lastRowNum = sheet.getLastRowNum();
            Field[] fields = target.getDeclaredFields();
            for (int i = startRow; i <= lastRowNum; i++) {
                //构造对象
                Constructor<T> constructor = target.getDeclaredConstructor();
                T targetObj = constructor.newInstance();

                Row currentRow = sheet.getRow(i);
                if (currentRow == null) {
                    //break;
                    continue;
                }

                //最后一个单元格的索引
                int cells = currentRow.getLastCellNum();
                for (int j = startCol; j < cells; j++) {
                    if (fields.length - 1 < j - startCol) {
                        continue;
                    }
                    Object data = Cells.parseValue(currentRow.getCell(j));
                    Field field = fields[j - startCol];
                    field.setAccessible(true);
                    field.set(targetObj, Converts.convert(data, field.getType()));
                }

                if (targetObj instanceof Empty empty) {
                    if (!empty.isEmpty()) {
                        list.add(targetObj);
                    }
                } else {
                    list.add(targetObj);
                }
            }
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(source);
        }
    }
}
