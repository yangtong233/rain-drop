package org.raindrop.common.enums;

/**
 * 响应状态码枚举(在http响应码为200的情况下)
 */
public enum Resp {
    SUCCESS(0, true, "请求成功"),
    NOT_LONGIN(1, true, "未登录，请先登录"),
    LONGIN_TIME_OUT(2, true, "登录过期，请重新登录"),
    UNKNOWN_USER(30, false, "用户不存在"),
    BANNED_USER(40, false, "被禁止的用户"),
    PASSWORD_ERROR(50, false, "密码错误"),
    CAPTCHA_ERROR(60, false, "验证码错误"),
    DATA_FORMAT_ERROR(70, false, "数据格式错误"),
    NULL_KEY(80, false, "key不能为空"),
    TYPE_ERROR(90, false, "类型异常"),
    OTHER_ERROR(100, false, "其他异常");

    /**
     * 请求结果状态码
     */
    public final Integer code;
    /**
     * 请求是否成功
     */
    public final Boolean status;
    /**
     * 请求结果状态说明
     */
    public final String message;

    Resp(Integer code, Boolean status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "code:" + code +
                ", msg:'" + message + '\'' +
                '}';
    }
}
