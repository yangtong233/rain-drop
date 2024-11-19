package org.raindrop.core.storage;

/**
 * created by yangtong on 2024/5/19 11:09
 *
 * @Description: 文件存储类型枚举
 */
public enum FileStorageType {

    LOCAL(1),
    MINIO(2);

    final Integer code;

    FileStorageType(Integer code) {
        this.code = code;
    }

}
