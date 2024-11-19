package org.raindrop.core.web.tool.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "AddOrEditCacheReq", description = "新增或编辑缓存的请求类型")
public class AddOrEditCacheReq {

    @Schema(name = "缓存key")
    @NotNull
    String key;

    @Schema(name = "缓存value")
    @NotNull
    Object value;

    @Schema(name = "过期时间，单位(s)")
    Long timeout;

}
