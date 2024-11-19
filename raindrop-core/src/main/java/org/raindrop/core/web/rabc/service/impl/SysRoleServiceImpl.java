package org.raindrop.core.web.rabc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.raindrop.common.utils.collection.Colls;
import org.raindrop.core.web.rabc.mapper.SysRoleMapper;
import org.raindrop.core.web.rabc.mapper.SysUserRoleMapper;
import org.raindrop.core.web.rabc.model.po.SysRole;
import org.raindrop.core.web.rabc.model.po.SysUserRole;
import org.raindrop.core.web.rabc.model.resp.SysUserRoleResp;
import org.raindrop.core.web.rabc.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<SysRole> listRoles(String userId) {
        return baseMapper.listRoles(userId);
    }

    @Override
    public List<SysUserRoleResp> listUserRole(String userId) {
        //先得到所有角色
        List<SysRole> roles = this.list(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleStatus, true));

        //再得到当前用户的角色
        List<SysUserRole> userRoles = userRoleMapper.listRoleByUserId(userId);

        //映射SysUserRoleResp
        return roles.stream().map(role -> {
            SysUserRoleResp resp = new SysUserRoleResp()
                    .setRoleId(role.getId())
                    .setRoleCode(role.getRoleCode())
                    .setRoleName(role.getRoleName());
            SysUserRole userRole = Colls.getOne(userRoles, item -> item.getRoleId().equals(role.getId()));
            if (userRole != null) {
                resp.setUserId(userRole.getUserId())
                        .setUserName(userRole.getUserName());
            }
            return resp;
        }).toList();

    }

    @Override
    public void assignRole(SysUserRole userRole) {
        userRole.setId(null);
        userRoleMapper.insert(userRole);
    }

    @Override
    public void cancelRole(SysUserRole userRole) {
        userRoleMapper.delete(
                Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, userRole.getUserId())
                .eq(SysUserRole::getRoleId, userRole.getRoleId())
        );
    }
}
