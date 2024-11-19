package org.raindrop.core.web.rabc.model.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.raindrop.core.web.rabc.model.po.SysPermission;
import org.raindrop.core.web.rabc.model.po.SysRole;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户信息
 */
@Data
public class OnlineUser {
    /**
     * ================以下数据，在用户登录成功后会更新
     */
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 登录时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime loginTime;
    /**
     * 持续在线时间，单位s
     */
    private long lastingOnlineTime;
    /**
     * 当前用户的标识令牌
     */
    private String token;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 备注
     */
    private String remark;
    /**
     * 该用户角色
     */
    private List<SysRole> roles;

    /**
     * 该用户拥有资源
     */
    private List<SysPermission> permissions;

    /**
     * ================以下数据，在每次请求后会更新
     */
    /**
     * 该用户上一次请求时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastRequestTime;
    /**
     * 记录上一次请求耗时
     */
    private Long spendTime;
    /**
     * 在线期间请求此次数
     */
    private int requestCount;

    public long getLastingOnlineTime() {
        if (this.loginTime != null) {
            Duration duration = Duration.between(loginTime, LocalDateTime.now());
            return duration.toSeconds();
        }
        return -1;
    }

}
