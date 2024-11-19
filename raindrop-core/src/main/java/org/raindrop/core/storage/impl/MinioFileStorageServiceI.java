package org.raindrop.core.storage.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.core.storage.FileStorageType;
import org.raindrop.core.storage.FileUploadInfo;
import org.raindrop.core.storage.IFileStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by yt on 2024/4/9 22:36
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "drop.storage.type", havingValue = "org.raindrop.core.storage.impl.MinioFileStorageServiceI")
public class MinioFileStorageServiceI implements IFileStorageService {

    @PostConstruct
    public void init() {
        System.out.println();
    }

    @Override
    public FileUploadInfo upload(MultipartFile file) {
        return null;
    }

    @Override
    public void download(String fileId) {

    }

    @Override
    public void delete(String fileId) {

    }

    @Override
    public File exist(String fileId) {
        return null;
    }

    @Override
    public FileStorageType getType() {
        return FileStorageType.MINIO;
    }
}
