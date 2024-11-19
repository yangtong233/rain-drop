package org.raindrop.core.web.rabc.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 角色-权限关系
 */
@Data
@Schema(name = "角色资源信息", description = "角色-资源对照信息")
public class SysRolePermissionResp {
    private String label;
    private String roleId;
    private String permissionId;
    //父权限id
    private String pid;
    //0-添加角色，1-取消角色
    private Integer status;

    private List<SysRolePermissionResp> children;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRolePermissionResp vo = (SysRolePermissionResp) o;
        return vo.permissionId.equals(this.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId);
    }
}