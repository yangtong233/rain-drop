package org.raindrop.core.web.rabc.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.common.web.PageReq;

@Data
@Schema(name = "用户列表数据传输对象")
public class SysUserReq extends PageReq {
    @Schema(name = "用户列表数据传输对象")
    private String userName;

    @Schema(name = "真实姓名")
    private String realName;

    @Schema(name = "状态")
    private Boolean status;
}
