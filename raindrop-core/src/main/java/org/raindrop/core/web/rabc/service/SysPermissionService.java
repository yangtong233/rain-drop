package org.raindrop.core.web.rabc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.po.SysRolePermission;
import org.raindrop.core.web.rabc.model.req.PermissionResetReq;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 分页查询资源的树状数据
     * @return 权限资源的树形数据
     */
    IPage<SysPermission> pageTree(String name, String code, Integer type);

    /**
     * 查找指定资源的树状数据
     * @param userId 用户id
     * @return 指定用户权限资源的树形数据
     */
    List<SysPermission> listPermissionsByUserId(String userId);

    /**
     * 根据角色id得到所有角色权限关系数据
     */
    List<SysRolePermission> listRolePermission(String roleId);

    /**
     * 给指定角色分配指定资源
     * @param rolePermission 角色-资源对照对象
     */
    void assignPermissionToRole(SysRolePermission rolePermission);

    /**
     * 取消指定角色的指定资源
     * @param rolePermission 角色-资源对照对象
     */
    void cancelPermission(SysRolePermission rolePermission);

    /**
     * 重置指定角色的资源
     */
    void resetPermission(PermissionResetReq permissionResetReq);
}
