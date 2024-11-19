package org.raindrop.core.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件持久化存储规范
 * Created by yt on 2024/4/9 21:32
 */
public interface IFileStorageService {
    /**
     * 上传文件
     * @param file 文件
     * @return 文件信息对象
     */
    FileUploadInfo upload(MultipartFile file);

    /**
     * 根据文件id响应文件二进制流
     * @param fileId 文件id
     */
    void download(String fileId);

    /**
     * 根据文件id删除文件
     * @param fileId 文件id
     */
    void delete(String fileId);

    /**
     * 判断指定文件是否存在
     * @param fileId 文件id
     * @return 如果文件存在返回File对象，不存在返回null
     */
    File exist(String fileId);

    /**
     * 获取文件存储类型
     * @return 文件存储类型
     */
    FileStorageType getType();
}
