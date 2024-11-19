package org.raindrop.core.web.rabc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.raindrop.common.utils.string.Strs;
import org.raindrop.common.web.BaseController;
import org.raindrop.common.web.R;
import org.raindrop.common.utils.web.PageUtil;
import org.raindrop.core.web.rabc.model.po.SysRole;
import org.raindrop.core.web.rabc.model.po.SysUserRole;
import org.raindrop.core.web.rabc.service.SysRoleService;
import org.raindrop.core.web.rabc.model.resp.SysUserRoleResp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色接口
 */
@RestController
@RequestMapping("/rabc/role")
@AllArgsConstructor
@Tag(name = "角色")
public class SysRoleController extends BaseController<SysRole, SysRoleService> {

    private SysRoleService roleService;

    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("list")
    public R<IPage<SysRole>> list(String roleName, String roleCode, Boolean roleStatus) {
        return R.success(roleService.page(PageUtil.getPage(),
                Wrappers.<SysRole>lambdaQuery()
                        .eq(Strs.isNotEmpty(roleCode), SysRole::getRoleCode, roleCode)
                        .eq(roleStatus != null, SysRole::getRoleStatus, roleStatus)
                        .like(Strs.isNotEmpty(roleName), SysRole::getRoleName, roleName))
        );
    }

    @Operation(summary = "查询角色", description = "根据用户id查询该用户的角色拥有信息")
    @GetMapping("listUserRole")
    public R<List<SysUserRoleResp>> listUserRole(String userId) {
        return R.success(roleService.listUserRole(userId));
    }

    @Operation(summary = "给用户分配角色", description = "给指定用户分配指定角色")
    @PostMapping("assignRole")
    public R<String> assignRole(@RequestBody SysUserRole userRole) {
        roleService.assignRole(userRole);
        return R.success("分配成功");
    }

    @Operation(summary = "取消用户的角色", description = "取消指定用户的指定角色")
    @PostMapping("cancelRole")
    public R<String> cancelRole(@RequestBody SysUserRole userRole) {
        roleService.cancelRole(userRole);
        return R.success("取消成功");
    }
}
