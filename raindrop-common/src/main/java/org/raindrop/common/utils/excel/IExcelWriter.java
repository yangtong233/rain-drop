package org.raindrop.common.utils.excel;

import java.io.OutputStream;

/**
 * created by yangtong on 2024/6/6 下午12:51
 *
 * @Description: Excel文件写入接口
 */
public interface IExcelWriter {

    /**
     * 从指定的路径读取模板文件
     * @param classPath 模板文件路径
     * @return this
     */
    IExcelWriter from(String classPath);

    /**
     * 将生产的excel写入到指定的输出流
     * @param targetStream 输出流
     * @return this
     */
    IExcelWriter to(OutputStream targetStream);

    /**
     * 执行写入操作，确保前两步都完成了
     */
    void doWrite();
}
