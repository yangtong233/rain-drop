package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.common.utils.tree.BaseTree;
import org.raindrop.common.web.PersistenceModel;

/**
 * created by yangtong on 2025/2/11 17:41:20
 */
@Data
@TableName("sys_depart")
@Schema(name = "系统部门", description = "系统部门")
public class SysDepart extends BaseTree<SysDepart> {

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String departName;

    /**
     * 部门编码
     * 编码规则，当前部门编码 = 父级部门编码 + 2位顺序号
     * eg:
     *    父级部门编码是 A01，该父级部门下有3个子部门
     *    则该父级部门的第4个子部门的编码是 A0104
     *    同理，A0104的第一个子部门的编码是 A010401
     */
    @Schema(description = "部门编码")
    private String departCode;

    /**
     * 部门状态，是否启用
     */
    @Schema(description = "部门状态")
    private Boolean status;

    /**
     * 部门序号
     */
    @Schema(description = "部门序号")
    private Integer sort;

    /**
     * 部门备注
     */
    @Schema(description = "部门备注")
    private String remark;

}
