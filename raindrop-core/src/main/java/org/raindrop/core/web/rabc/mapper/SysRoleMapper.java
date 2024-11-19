package org.raindrop.core.web.rabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.raindrop.core.web.rabc.model.po.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户id查出角色信息
     * @param userId 用户id
     * @return 角色信息数组
     */
    List<SysRole> listRoles(String userId);
}
