package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.common.web.PersistenceModel;

/**
 * created by yangtong on 2025/2/11 17:53:27
 */
@Data
@TableName("sys_user_depart")
@Schema(name = "用户-部门映射关系", description = "用户-部门映射关系")
public class SysUserDepart extends PersistenceModel {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String userId;

    /**
     * 部门id
     */
    @Schema(description = "部门id")
    private String departId;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
