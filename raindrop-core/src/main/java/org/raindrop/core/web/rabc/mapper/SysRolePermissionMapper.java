package org.raindrop.core.web.rabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.raindrop.core.web.rabc.model.po.SysRolePermission;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
    /**
     * 查询该角色拥有的资源数据
     * @param roleId 角色id
     * @return
     */
    List<SysRolePermission> listRolePermission(String roleId);
}
