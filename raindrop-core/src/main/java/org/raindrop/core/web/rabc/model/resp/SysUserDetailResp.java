package org.raindrop.core.web.rabc.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.po.SysRole;

import java.util.List;

/**
 * 展示给前端显示的系统用户详情
 */
@Data
@Schema(name = "用户详情")
public class SysUserDetailResp {
    /**
     * 主键id
     */
    @Schema(name = "用户id")
    private String id;
    /**
     * 登录使用账号，唯一
     */
    @Schema(name = "用户名")
    private String userName;
    /**
     * 选填
     */
    @Schema(name = "用户真实姓名")
    private String realName;
    /**
     * 图片base64编码，选填
     */
    @Schema(name = "用户头像")
    private String avatar;
    /**
     * 选填
     */
    @Schema(name = "手机号")
    private String phone;
    /**
     * 同上
     */
    @Schema(name = "邮箱")
    private String email;
    /**
     * 这个看着填
     */
    @Schema(name = "备注")
    private String remark;
    /**
     * 一个用户对应多个角色
     */
    @Schema(name = "该用户角色")
    private List<SysRole> roles;
    /**
     * 一个角色对应多个资源
     */
    @Schema(name = "该用户拥有资源")
    private List<SysPermission> permissions;
}
