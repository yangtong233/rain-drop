package org.raindrop.core.auth;

import lombok.Data;
import org.raindrop.core.web.rabc.model.resp.OnlineUser;

/**
 * 保存当前请求用户的上下文信息
 */
@Data
public class U {

    private static final ThreadLocal<OnlineUser> holder = new ThreadLocal<>();

    public static void set(OnlineUser onlineUser) {
        holder.set(onlineUser);
    }

    public static OnlineUser get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }

    public static String token() {
        OnlineUser onlineUser = holder.get();
        return onlineUser.getToken();
    }

}
