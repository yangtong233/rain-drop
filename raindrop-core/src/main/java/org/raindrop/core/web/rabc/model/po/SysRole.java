package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.raindrop.common.web.PersistenceModel;

/**
 * 角色表模型，通过sys_role_permission与sys_permission进行关联，多对多
 */
@Data
@TableName("sys_role")
public class SysRole extends PersistenceModel {
    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;
    /**
     * 角色编码
     */
    @TableField(value = "role_code")
    private String roleCode;
    /**
     * 角色状态
     */
    @TableField(value = "role_status")
    private Boolean roleStatus;
    /**
     * 角色备注
     */
    @TableField(value = "role_remark")
    private String roleRemark;
}

