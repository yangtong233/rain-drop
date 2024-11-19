package org.raindrop.core.web.rabc.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户-角色对应关系
 */
@Data
@Accessors(chain = true)
@Schema(name = "用户角色信息", description = "展示用户-角色之间的对应关系")
public class SysUserRoleResp {
    /**
     * 用户id
     */
    @Schema(name = "用户id")
    private String userId;
    /**
     * 角色id
     */
    @Schema(name = "角色id")
    private String roleId;

    /**
     * 角色编码
     */
    @Schema(name = "角色编码")
    private String roleCode;

    /**
     * 用户名称
     */
    @Schema(name = "用户名称")
    private String userName;
    /**
     * 角色名称
     */
    @Schema(name = "角色名称")
    private String roleName;

}
