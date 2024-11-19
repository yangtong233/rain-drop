package org.raindrop.core.web.tool.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.raindrop.common.web.R;
import org.raindrop.core.storage.F;
import org.raindrop.core.storage.FileUploadInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * created by yangtong on 2024/5/25 16:02
 *
 * @Description: 系统文件接口
 */
@RestController
@RequestMapping("/sys/file")
@Tag(name = "系统文件接口")
public class SysFileController {

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public R<FileUploadInfo> upload(@RequestPart MultipartFile file) {
        FileUploadInfo upload = F.upload(file);
        return R.success(upload);
    }

    @GetMapping("/download")
    @Operation(summary = "下载文件")
    public void upload(String fileId) {
        F.download(fileId);
    }

    @PostMapping("/deleteFile")
    @Operation(summary = "删除文件")
    public R<String> deleteFile(String fileId) {
        F.delete(fileId);
        return R.success("删除成功");
    }

}
