package org.raindrop.core.web.rabc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.raindrop.core.web.rabc.model.req.SysUserReq;
import org.raindrop.core.web.rabc.model.po.SysUser;
import org.raindrop.core.web.rabc.model.resp.SysUserDetailResp;
import org.raindrop.core.web.rabc.model.resp.SysUserResp;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public interface SysUserService extends IService<SysUser> {
    /**
     * 查询用户信息
     */
    IPage<SysUserResp> listUser(SysUserReq sysUserReq);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return
     */
    SysUserResp queryById(String id);

    /**
     * 根据用户id得到用户详细信息
     * @param id
     * @return
     */
    SysUserDetailResp getDetailById(String id);

    /**
     * 根据用户基本信息返回用户详细信息
     * @param user
     * @return
     */
    SysUserDetailResp getDetailByUser(SysUser user);

    /**
     * 用户，新增或编辑
     */
    void saveOrUpdateUser(SysUser user);

    /**
     * 根据id删除用户
     */
    void deleteUserById(String id);


}
