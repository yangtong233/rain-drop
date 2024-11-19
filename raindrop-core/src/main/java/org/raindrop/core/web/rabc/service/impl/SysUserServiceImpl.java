package org.raindrop.core.web.rabc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.raindrop.common.utils.web.PageUtil;
import org.raindrop.core.auth.password.Identity;
import org.raindrop.core.web.rabc.mapper.SysUserMapper;
import org.raindrop.core.web.rabc.mapper.SysUserRoleMapper;
import org.raindrop.core.web.rabc.model.req.SysUserReq;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.po.SysRole;
import org.raindrop.core.web.rabc.model.po.SysUser;
import org.raindrop.core.web.rabc.model.po.SysUserRole;
import org.raindrop.core.web.rabc.model.resp.SysUserDetailResp;
import org.raindrop.core.web.rabc.model.resp.SysUserResp;
import org.raindrop.core.web.rabc.service.SysPermissionService;
import org.raindrop.core.web.rabc.service.SysUserService;
import org.springframework.stereotype.Service;
import org.raindrop.core.web.rabc.service.SysRoleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private Identity identity;
    private SysUserRoleMapper userRoleMapper;
    private SysRoleService roleService;
    private SysPermissionService permissionService;

    @Override
    public IPage<SysUserResp> listUser(SysUserReq sysUserReq) {
        IPage<SysUser> page = this.page(PageUtil.getPage(), Wrappers.<SysUser>lambdaQuery()
                .eq(sysUserReq.getStatus() != null, SysUser::getStatus, sysUserReq.getStatus())
                .like(StrUtil.isNotEmpty(sysUserReq.getUserName()), SysUser::getUserName, sysUserReq.getUserName())
                .like(StrUtil.isNotEmpty(sysUserReq.getRealName()), SysUser::getRealName, sysUserReq.getRealName())
        );
        List<SysUserResp> records = page.getRecords().stream().map(user -> {
            SysUserResp sysUserResp = BeanUtil.copyProperties(user, SysUserResp.class);
            List<String> r = baseMapper.listRolesByUserId(user.getId());
            sysUserResp.setRoles(r);
            return sysUserResp;
        }).collect(Collectors.toList());

        return PageUtil.transPage(page, records);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户数据
     */
    @Override
    public SysUserResp queryById(String id) {
        SysUser user = this.getById(id);
        SysUserResp sysUserResp = BeanUtil.copyProperties(user, SysUserResp.class);
        List<String> r = baseMapper.listRolesByUserId(user.getId());
        sysUserResp.setRoles(r);
        return sysUserResp;
    }

    @Override
    public SysUserDetailResp getDetailById(String id) {
        SysUser user = this.getById(id);
        return getDetailByUser(user);
    }

    @Override
    public SysUserDetailResp getDetailByUser(SysUser user) {
        SysUserDetailResp detail = BeanUtil.copyProperties(user, SysUserDetailResp.class);
        List<SysRole> roles = roleService.listRoles(user.getId());
        List<SysPermission> permissions = permissionService.listPermissionsByUserId(user.getId());
        detail.setRoles(roles);
        detail.setPermissions(permissions);
        return detail;
    }

    @Override
    public void saveOrUpdateUser(SysUser user) {
        if (StrUtil.isEmpty(user.getPassword())) {
            user.setPassword(identity.encodePassword(user.getPassword()));
        }
        this.saveOrUpdate(user);
    }

    @Override
    public void deleteUserById(String id) {
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id));
        this.removeById(id);
    }
}
