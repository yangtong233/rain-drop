package org.raindrop.core.web.rabc.model.resp;

import lombok.Data;

import java.util.List;

/**
 * 分配权限
 */
@Data
public class AssignPermissionResp {
    private String roleId;
    private String permissionId;
}
