package org.raindrop.core.web.rabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.raindrop.core.web.rabc.model.po.SysUser;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id查询所有角色名称
     * @param id
     * @return
     */
    List<String> listRolesByUserId(String id);
}
