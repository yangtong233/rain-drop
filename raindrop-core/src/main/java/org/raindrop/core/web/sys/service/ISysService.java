package org.raindrop.core.web.sys.service;

import org.raindrop.core.web.rabc.model.resp.SysUserDetailResp;
import org.raindrop.core.web.sys.model.req.LoginReq;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public interface ISysService {

    /**
     * 登录
     * @param userLogin 用户登录时提交的账号密码验证码登信息
     * @return 登录从时返回该用户的详细信息，失败时返回空
     */
    SysUserDetailResp login(LoginReq userLogin);
}
