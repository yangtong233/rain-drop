package org.raindrop.core.web.rabc.model.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 页面展示数据
 */
@Data
@Schema(name = "SysUserVo", description = "页面展示数据")
public class SysUserResp {
    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String id;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String userName;
    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;
    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean status;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
    /**
     * 创建日期
     */
    @Schema(description = "创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    @Schema(description = "修改日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 该用户拥有的角色名称
     */
    @Schema(description = "用户id")
    private List<String> roles;
}
