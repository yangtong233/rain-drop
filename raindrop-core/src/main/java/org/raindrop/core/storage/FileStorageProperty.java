package org.raindrop.core.storage;

import lombok.Data;
import org.raindrop.common.utils.string.Strs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * Created by yt on 2024/4/9 21:49
 * 文件存储配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "drop.storage")
public class FileStorageProperty {
    /**
     * 文件存储类型，也就是IFileStorageService的一个实现类
     */
    private Class<? extends IFileStorageService> type;

    /**
     * 文件最大上传的字节数(byte)
     */
    private String max;
    /**
     * 一些配置项
     */
    private Map<String, String> config;

    /**
     * 这个方法返回文件的最大上传容量。
     * 如果max属性为null，它默认为100MB。
     * max属性被转换为大写，然后根据单位（KB，MB，GB，TB）确定适当的乘数。
     * 然后，max属性的数字部分乘以乘数，以字节为单位计算最终值。
     *
     * @return 以字节为单位的最大上传容量。
     */
    public Long parseMax() {
        max = max.toUpperCase(Locale.ROOT);
        if (Strs.isNotEmpty(max)) {
            if (max.endsWith("KB")) {
                long _max = Integer.parseInt(max.replace("KB", ""));
                return 1024 * _max;
            }
            if (max.endsWith("MB")) {
                long _max = Integer.parseInt(max.replace("MB", ""));
                return 1024 * 1024 * _max;
            }
            if (max.endsWith("GB")) {
                long _max = Integer.parseInt(max.replace("GB", ""));
                return 1024 * 1024 * 1024 * _max;
            }

        }
        //默认100MB
        return 100 * 1024 * 1024L;
    }
}
