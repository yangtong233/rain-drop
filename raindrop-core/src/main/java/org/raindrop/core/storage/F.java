package org.raindrop.core.storage;

import org.raindrop.common.exception.BaseException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件持久化静态工具类
 * Created by yt on 2024/4/9 21:31
 */
@Component
public class F {
    private static IFileStorageService fileStorageService;

    public F(IFileStorageService fileStorageService) {
        F.fileStorageService = fileStorageService;
    }

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件信息对象
     */
    public static FileUploadInfo upload(MultipartFile file) {
        check();
        return fileStorageService.upload(file);
    }

    /**
     * 根据文件id响应文件二进制流
     * @param fileId 文件id
     */
    public static void download(String fileId){
        check();
        fileStorageService.download(fileId);
    }

    /**
     * 根据文件id删除文件
     * @param fileId 文件id
     */
    public static void delete(String fileId) {
        check();
        fileStorageService.delete(fileId);
    }

    /**
     * 判断指定文件是否存在
     * @param fileId 文件id
     */
    public static File exist(String fileId) {
        check();
        return fileStorageService.exist(fileId);
    }

    /**
     * 使用F的静态方法之前，检测fileStorageService是否被注入
     */
    private static void check() {
        if (fileStorageService == null) {
            throw new BaseException("确保在spring上下文中使用F类的静态方法");
        }
    }
}
