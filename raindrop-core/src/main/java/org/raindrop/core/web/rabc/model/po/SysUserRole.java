package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.raindrop.common.web.PersistenceModel;

/**
 * 角色权限表模型，关联sys_user与sys_role
 */
@Data
@TableName("sys_user_role")
public class SysUserRole extends PersistenceModel {
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String roleId;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;
}
