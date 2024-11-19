package org.raindrop.common.enums;

/**
 * 系统无需鉴权的公共接口
 */
public enum PublicResource {
    CAPTCHA("/sys/getCaptcha"),
    LOGIN("/sys/login"),
    LOGOUT("/sys/logout"),
    TOKEN_TIME_OUT("/sys/getTokenTimeOut"),
    SYS_STATUS_CODE("/sys/listResCode"),
    SYS_PUBLIC_RESOURCE("/sys/listSysPublicResource"),
    UPLOAD_FILE("/sys/upload"),
    DOWNLOAD_FILE("/sys/download"),
    DELETE_FILE("/sys/deleteFile");

    /**
     * 资源名称
     */
    public final String resource;

    PublicResource(String resource) {
        this.resource = resource;
    }
}
