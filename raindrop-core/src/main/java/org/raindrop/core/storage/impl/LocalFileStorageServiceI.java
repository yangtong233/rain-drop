package org.raindrop.core.storage.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.exception.BaseException;
import org.raindrop.core.storage.FileStorageProperty;
import org.raindrop.core.storage.FileStorageType;
import org.raindrop.core.storage.FileUploadInfo;
import org.raindrop.core.storage.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 以本地磁盘作为文件持久化存储介质
 * Created by yt on 2024/4/9 21:43
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "drop.storage.type", havingValue = "org.raindrop.core.storage.impl.LocalFileStorageServiceI")
public class LocalFileStorageServiceI implements IFileStorageService {

    @Autowired
    private FileStorageProperty fileStorageProperty;

    /**
     * 文件上传路径
     */
    private String path;

    @PostConstruct
    public void init() {
        String path = fileStorageProperty.getConfig().get("path");
        if (StrUtil.isEmpty(path)) {
            path = "file-storage/";
        }
        this.path = path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        log.info("本地文件存储路径:{}", file.getAbsoluteFile());
    }

    @Override
    public FileUploadInfo upload(MultipartFile file) {
        FileUploadInfo fileUploadInfo = new FileUploadInfo();
        fileUploadInfo.setFileName(file.getOriginalFilename());
        fileUploadInfo.setFileSize(file.getSize());
        try {
            String today = DateUtil.format(new Date(), "yyyy-MM-dd");
            File targetPath = new File(path, today);
            if (!targetPath.exists()) {
                targetPath.mkdirs();
            }
            File targetFile = new File(targetPath, file.getOriginalFilename());
            targetFile.createNewFile();
            //file.transferTo(targetFile);
            InputStream is = file.getInputStream();
            FileOutputStream os = new FileOutputStream(targetFile);
            IoUtil.copy(is, os);
            is.close();
            os.close();
            //文件id设为文件夹名称 + "-" + 文件名称
            fileUploadInfo.setFileId(today + "#" + file.getOriginalFilename());
        } catch (IOException e) {
            throw new BaseException(e);
        }
        return fileUploadInfo;
    }

    @Override
    public void download(String fileId) {
        File file = exist(fileId);
        if (file == null) {
            throw new BaseException("文件不存在");
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        try(FileInputStream fis = new FileInputStream(file)) {
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8).replace("+", "%20");
            String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName;
            response.setContentType("application/octet-stream");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

            IoUtil.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(String fileId) {
        File file = exist(fileId);
        if (file != null) {
            file.delete();
        }
    }

    @Override
    public File exist(String fileId) {
        File file = getFileById(fileId);
        return file.exists() ? file : null;
    }

    @Override
    public FileStorageType getType() {
        return FileStorageType.LOCAL;
    }

    private File getFileById(String fileId) {
        if (StrUtil.isBlank(fileId)) {
            throw new BaseException("文件id不能为空");
        }
        String[] fileInfos = fileId.split("#", 2);
        if (fileInfos.length != 2) {
            throw new BaseException("文件id格式非法");
        }
        String date = fileInfos[0];
        String fileName = fileInfos[1];

        return new File(path, date + File.separator + fileName);
    }
}
