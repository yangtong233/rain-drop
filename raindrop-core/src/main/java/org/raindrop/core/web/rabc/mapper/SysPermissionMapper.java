package org.raindrop.core.web.rabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.resp.SysRolePermissionResp;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 根据用户id查询该用户的权限集合
     * @param userId
     * @return
     */
    List<SysPermission> listPermissionsByUserId(String userId);

    /**
     * 根据角色id得到所有角色权限关系数据
     * @param roleId 角色id
     * @return 关系数组
     */
    List<SysRolePermissionResp> listRolePermission(String roleId);
}
