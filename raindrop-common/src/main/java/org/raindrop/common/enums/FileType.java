package org.raindrop.common.enums;

import org.raindrop.common.utils.digit.Digits;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 文件类型枚举
 */
public enum FileType {
    XLS("0xD0CF11E0", "xls"),
    XLSX("0x504B0304", "xlsx"),
    DOC("0xD0CF11E0", "doc"),
    DOCX("0x504B0304", "docx"),
    PNG("0x89504E47", "png"),
    JPEG("0xFFD8FF", "jpg"),
    UNKNOWN("0xFFFFFFFFFFFFFFFFFFFF", "unknown");

    String magicNum;
    final String suffix;

    FileType(String magicNum, String suffix) {
        this.magicNum = magicNum;
        this.suffix = suffix;
    }

    public static FileType getFileType(InputStream is) {
        byte[] buffer = new byte[20];
        int readSize;
        try {
            is.mark(20);
            readSize = is.read(buffer, 0, 20);
            String currentMagicNum = Digits._10To16(buffer);
            for (FileType type : FileType.values()) {
                if (type.magicNum.length() <= currentMagicNum.length() && currentMagicNum.startsWith(type.magicNum)) {
                    //将流恢复到原始状态
                    is.reset();
                    return type;
                }
            }
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
        UNKNOWN.magicNum = new String(buffer, 0, readSize);
        return UNKNOWN;
    }

}
