package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.raindrop.common.utils.tree.BaseTree;

/**
 * 角色权限表模型
 */
@Data
@Accessors(chain = true)
@TableName("sys_role_permission")
public class SysRolePermission extends BaseTree<SysRolePermission> {
    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String roleId;
    /**
     * 资源id
     */
    @TableField(value = "permission_id")
    private String permissionId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    @TableField(exist = false)
    private String roleName;

    @Schema(description = "角色编码")
    @TableField(exist = false)
    private String roleCode;

    @Schema(description = "资源名称")
    @TableField(exist = false)
    private String permissionName;

    @Schema(description = "资源编码")
    @TableField(exist = false)
    private String permissionCode;

}
