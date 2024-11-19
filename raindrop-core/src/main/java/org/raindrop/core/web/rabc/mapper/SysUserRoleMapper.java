package org.raindrop.core.web.rabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.raindrop.core.web.rabc.model.po.SysUserRole;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 获取指定用户的所有角色
     * @param userId 用户id
     * @return 用户-角色集合
     */

    List<SysUserRole> listRoleByUserId(String userId);
}
