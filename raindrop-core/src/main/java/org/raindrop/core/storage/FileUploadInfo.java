package org.raindrop.core.storage;

import lombok.Data;
import org.raindrop.common.enums.FileType;

/**
 * 上次到文件服务器的文件信息
 * Created by yt on 2024/4/9 21:33
 */
@Data
public class FileUploadInfo {
    /**
     * 文件id，全局唯一，根据该id可以找到一个唯一对应的文件
     */
    private String fileId;

    /**
     * 文件大小，单位字节
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private FileType fileType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件备注
     */
    private String remark;

}
