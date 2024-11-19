package org.raindrop.core.web.rabc.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "PermissionResetReq", description = "重置角色的资源")
public class PermissionResetReq {
    @Schema(name = "角色id")
    private String roleId;

    @Schema(name = "资源id")
    private List<String> permissionIds;
}
