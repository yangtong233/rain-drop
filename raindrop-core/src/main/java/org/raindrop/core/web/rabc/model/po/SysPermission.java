package org.raindrop.core.web.rabc.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.common.utils.tree.BaseTree;

import java.util.List;

/**
 * 资源权限表模型
 */
@Data
@TableName("sys_permission")
@Schema(name = "资源权限", description = "资源权限")
public class SysPermission extends BaseTree<SysPermission> {

    /**
     * 父资源id，顶级资源pid为0
     */
    @TableField(value = "pid")
    private String pid;
    /**
     * 资源类型,0-按钮，1-菜单
     */
    @TableField(value = "type")
    private String type;
    /**
     * 资源名称，展示在页面上
     */
    @TableField(value = "name")
    private String name;
    /**
     * 资源编码，区分唯一的资源，资源名称可以重复，但是编码不能重复
     * 用作：路由名称，按钮标识符等
     */
    @TableField(value = "code")
    private String code;
    /**
     * 组件路由地址，type=1该字段才有意义
     * eg:
     *   /sys/user
     */
    @TableField(value = "router_path")
    private String routerPath;
    /**
     * 该路由对应的文件路径，type=1该字段才有意义
     * eg:
     *   layout目录下：/layout/index.vue
     *   view目录下:/view/sys/user/index.vue
     */
    @TableField(value = "router_component")
    private String routerComponent;
    /**
     * 可见性 visible
     */
    @TableField(value = "visible")
    private Boolean visible;
    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 重定向地址，仅针对路由
     */
    @TableField(value = "redirect")
    private String redirect;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 子资源
     */
    @TableField(exist = false)
    private List<SysPermission> children;

}
