package org.raindrop.core.web.rabc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.raindrop.core.web.rabc.model.po.SysRole;
import org.raindrop.core.web.rabc.model.po.SysUserRole;
import org.raindrop.core.web.rabc.model.resp.SysUserRoleResp;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据用户id得到该用户的所有角色信息
     */
    List<SysRole> listRoles(String userId);

    /**
     * 根据用户id得到该用户的用户-角色情况数组
     */
    List<SysUserRoleResp> listUserRole(String userId);

    /**
     * 给用户添加或取消角色
     */
    void assignRole(SysUserRole userRole);

    /**
     * 取消用户的角色
     */
    void cancelRole(SysUserRole userRole);
}
