package org.raindrop.core.web.rabc.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.raindrop.common.web.BaseController;
import org.raindrop.common.web.R;
import org.raindrop.core.cache.C;
import org.raindrop.core.web.rabc.model.po.SysUser;
import org.raindrop.core.web.rabc.model.req.SysUserReq;
import org.raindrop.core.web.rabc.model.resp.OnlineUser;
import org.raindrop.core.web.rabc.model.resp.SysUserDetailResp;
import org.raindrop.core.web.rabc.model.resp.SysUserResp;
import org.raindrop.core.web.rabc.service.SysPermissionService;
import org.raindrop.core.web.rabc.service.SysRoleService;
import org.raindrop.core.web.rabc.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 用户相关接口，需要认证。与登录不同，登录不需要认证，直接交给security管理
 */
@RestController
@RequestMapping("/rabc/user")
@AllArgsConstructor
@Tag(name = "用户")
public class SysUserController extends BaseController<SysUser, SysUserService> {

    private SysPermissionService permissionService;
    private SysRoleService roleService;

    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("list")
    public R<IPage<SysUserResp>> list(@ModelAttribute SysUserReq sysUserReq) {
        return R.success(baseService.listUser(sysUserReq));
    }

    @Operation(summary = "根据id删除数据", description = "如果有多个id则以逗号分开")
    @DeleteMapping("delete/{ids}")
    public R<String> delete(@Parameter(description = "主键id") @PathVariable String ids) {
        if (StrUtil.isNotEmpty(ids)) {
            //ids可能包含了多个id
            Arrays.stream(ids.split(",")).forEach(id -> baseService.deleteUserById(id));
        }
        return R.success("删除成功");
    }

    @GetMapping("/getUserDetailByToken")
    @Operation(summary = "根据token获取在线用户详情", description = "根据token获取在线用户详情")
    public R<SysUserDetailResp> getUserDetailByToken(@CookieValue("Authorization") String token) {
        OnlineUser onlineUser = C.get("onlineUser::" +token);
        SysUserDetailResp detail = baseService.getDetailById(onlineUser.getUserId());
        return R.success(detail);
    }

}
