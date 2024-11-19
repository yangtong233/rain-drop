package org.raindrop.core.web.rabc.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.raindrop.common.utils.web.PageUtil;
import org.raindrop.common.utils.collection.Colls;
import org.raindrop.common.utils.string.Strs;
import org.raindrop.common.utils.tree.Trees;
import org.raindrop.core.web.rabc.mapper.SysPermissionMapper;
import org.raindrop.core.web.rabc.mapper.SysRolePermissionMapper;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.po.SysRolePermission;
import org.raindrop.core.web.rabc.model.req.PermissionResetReq;
import org.raindrop.core.web.rabc.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限操作
 */
@Service
@AllArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    private SysRolePermissionMapper rolePermissionMapper;

    @Override
    public IPage<SysPermission> pageTree(String name, String code, Integer type) {
        //查询所有资源
        List<SysPermission> permissionList = this.list(
                Wrappers.<SysPermission>lambdaQuery()
                        .like(Strs.isNotEmpty(name), SysPermission::getName, name)
                        .eq(Strs.isNotEmpty(code), SysPermission::getCode, code)
                        .eq(type != null, SysPermission::getType, type)
                        .orderByAsc(SysPermission::getSort)
        );
        //转化为树状数据
        List<SysPermission> tree = Trees.toTree(permissionList);
        //进行分页
        return PageUtil.toPage(PageUtil.getPage(), tree);
    }

    /**
     * 根据用户id得到该用户的所有权限
     *
     * @param userId 用户id，为null则是查询所有
     * @return 拥有的权限集合
     */
    @Override
    public List<SysPermission> listPermissionsByUserId(String userId) {
        List<SysPermission> permissions;
        if (StrUtil.isNotEmpty(userId)) {
            permissions = baseMapper.listPermissionsByUserId(userId);
        } else {
            permissions = this.list(Wrappers.<SysPermission>lambdaQuery().orderByAsc(SysPermission::getSort));
        }
        return Trees.toTree(permissions);
    }

    @Override
    public void assignPermissionToRole(SysRolePermission rolePermission) {
        //增加角色资源，sys_role_permission表已经做了role_id与permission_id的唯一索引，所以直接新增
        rolePermissionMapper.insert(rolePermission);
    }

    @Override
    public void cancelPermission(SysRolePermission rolePermission) {
        //删除角色-资源对照数据
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery()
                .eq(SysRolePermission::getRoleId, rolePermission.getRoleId())
                .eq(SysRolePermission::getPermissionId, rolePermission.getPermissionId())
        );
    }

    @Override
    public void resetPermission(PermissionResetReq permissionResetReq) {
        //1.先删除该角色的所有资源
        String roleId = permissionResetReq.getRoleId();
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery()
                .eq(SysRolePermission::getRoleId, roleId));

        //2.再给该角色添加资源
        for (String permissionId : permissionResetReq.getPermissionIds()) {
            //TODO 以后优化成批量新增
            SysRolePermission rp = new SysRolePermission()
                    .setRoleId(roleId)
                    .setPermissionId(permissionId);
            rolePermissionMapper.insert(rp);
        }
    }

    @Override
    public List<SysRolePermission> listRolePermission(String roleId) {
        //1.查询所有资源数据
        List<SysPermission> list = this.list();

        //2.查询该角色拥有的资源数据
        List<SysRolePermission> rolePermissions = rolePermissionMapper.listRolePermission(roleId);

        //3.得到并返回角色-资源对照数据
        List<SysRolePermission> rolePermissionList = list.stream().map(permission -> {
            //资源
            SysRolePermission rolePermission = new SysRolePermission()
                    .setPermissionId(permission.getId())
                    .setPermissionCode(permission.getCode())
                    .setPermissionName(permission.getName());
            //为树形转化准备基础数据
            rolePermission.setId(permission.getId());
            rolePermission.setPid(permission.getPid());
            //角色
            SysRolePermission oneRp = Colls.getOne(
                    rolePermissions,
                    rp -> rp.getPermissionId().equals(permission.getId())
            );
            if (oneRp != null) {
                rolePermission
                        .setRoleId(oneRp.getRoleId())
                        .setRoleCode(oneRp.getRoleCode())
                        .setRoleName(oneRp.getRoleName());
            }
            return rolePermission;
        }).toList();

        //4.转为树形结构
        return Trees.toTree(rolePermissionList);
    }

}
