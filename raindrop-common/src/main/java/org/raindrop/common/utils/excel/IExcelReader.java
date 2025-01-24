package org.raindrop.common.utils.excel;

import java.io.InputStream;
import java.util.List;

/**
 * created by yangtong on 2025/1/24 13:57:12
 * Excel文件读取接口
 */
public interface IExcelReader<T> {

    /**
     * 从指定流读取
     */
    IExcelReader<T> from(InputStream inputStream);

    /**
     * 将每一行读取为指定对象target，并将数据结果装进result
     */
    IExcelReader<T> to(Class<T> target, List<T> result);

    /**
     * 开始执行读取
     */
    void doRead();

    /**
     * 从指定行指定列(从0开始)开始执行读取
     */
    void doRead(int startRow, int startCol);

}
