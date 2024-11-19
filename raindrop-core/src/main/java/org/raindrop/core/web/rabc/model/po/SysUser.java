package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.common.web.PersistenceModel;

/**
 * 用户表模型
 * 通过sys_user_role表与sys_role相关联，多对多
 */
@Data
@Schema(description = "用户表")
@TableName("sys_user")
public class SysUser extends PersistenceModel {
    /**
     * 用户名，必填
     */
    @Schema(description = "用户名", example = "username")
    @TableField(value = "user_name")
    private String userName;
    /**
     * 用户密码，必填
     */
    @Schema(description = "用户密码")
    @TableField(value = "password")
    private String password;
    /**
     * 真实姓名，选填
     */
    @Schema(description = "真实姓名")
    @TableField(value = "real_name")
    private String realName;
    /**
     * 用户头像，base64编码，选填
     */
    @Schema(description = "用户头像")
    @TableField(value = "avatar")
    private String avatar;
    /**
     * 用户电话，选填
     */
    @Schema(description = "用户电话")
    @TableField(value = "phone")
    private String phone;
    /**
     * 用户邮箱，选填
     */
    @Schema(description = "用户邮箱")
    @TableField(value = "email")
    private String email;
    /**
     * 用户状态，true-正常（默认），false-禁用
     */
    @Schema(description = "用户状态")
    @TableField(value = "status")
    private Boolean status;
    /**
     * 备注，选填
     */
    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;
}
