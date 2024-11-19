package org.raindrop.core.web.rabc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.raindrop.common.web.BaseController;
import org.raindrop.common.web.R;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.po.SysRolePermission;
import org.raindrop.core.web.rabc.model.req.PermissionResetReq;
import org.raindrop.core.web.rabc.service.SysPermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统权限接口
 */
@RestController
@RequestMapping("/rabc/permission")
@AllArgsConstructor
@Tag(name = "权限")
public class SysPermissionController extends BaseController<SysPermission, SysPermissionService> {
    private SysPermissionService permissionService;

    @Operation(summary = "分页查询资源树", description = "分页查询资源树")
    @GetMapping("pageTree")
    public R<IPage<SysPermission>> pageTree(String name, String code, Integer type) {
        return R.success(permissionService.pageTree(name, code, type));
    }

    @Operation(summary = "查询角色的资源", description = "根据角色id查询该角色-资源对照信息")
    @GetMapping("listRolePermission")
    public R<List<SysRolePermission>> listRolePermission(String roleId) {
        return R.success(permissionService.listRolePermission(roleId));
    }

    @Operation(summary = "重置资源", description = "重置指定角色的资源")
    @PostMapping("resetPermission")
    public R<String> resetPermission(@RequestBody PermissionResetReq permissionResetReq) {
        permissionService.resetPermission(permissionResetReq);
        return R.success("分配成功");
    }

    @Operation(summary = "分配资源", description = "给指定角色分配指定资源")
    @PostMapping("assignPermission")
    public R<String> assignPermissionToRole(@RequestBody SysRolePermission rolePermission) {
        permissionService.assignPermissionToRole(rolePermission);
        return R.success("分配成功");
    }

    @Operation(summary = "取消资源", description = "取消指定角色的指定资源")
    @PostMapping("cancelPermission")
    public R<String> cancelPermission(@RequestBody SysRolePermission rolePermission) {
        permissionService.cancelPermission(rolePermission);
        return R.success("取消成功");
    }

}
