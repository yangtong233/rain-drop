package org.raindrop.core.cache;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * created by yangtong on 2024/5/31 14:54
 *
 * @Description: 响应给前端的缓存实体
 */
@Data
@Accessors(chain = true)
@Schema(name = "CacheObj", description = "响应给前端的缓存实体")
public class CacheObjResp {

    @Schema(name = "缓存键")
    private String key;

    @Schema(name = "缓存值")
    private Object value;

    @Schema(name = "缓存值类型")
    private Class<?> type;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Schema(name = "缓存创建")
    private LocalDateTime createTime;

    @Schema(name = "缓存过期时间")
    private Long timeout;

}
