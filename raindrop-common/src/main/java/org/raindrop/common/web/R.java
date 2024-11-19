package org.raindrop.common.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.raindrop.common.enums.Resp;

/**
 * 统一返回对象，result
 *
 * @param <T>
 */
@Data
@Accessors(chain = true)
@Schema(description = "响应数据")
public class R<T> {
    @Schema(description = "请求是否成功")
    private Boolean success;
    @Schema(description = "消息")
    private String message;
    @Schema(description = "状态码")
    private Integer code;
    @Schema(description = "响应数据")
    private T data;

    private R() {}

    public static R<String> success() {
        return success(null);
    }

    public static R<String> success(String msg) {
        R<String> r = new R<>();
        r.success = true;
        r.message = msg;
        r.code = Resp.SUCCESS.code;
        r.data = msg;
        return r;
    }

    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.success = true;
        r.code = Resp.SUCCESS.code;
        r.data = data;
        return r;
    }

    public static <T> R<T> success(String msg, T data) {
        R<T> r = new R<>();
        r.success = true;
        r.message = msg;
        r.code = Resp.SUCCESS.code;
        r.data = data;
        return r;
    }

    public static <T> R<T> error() {
        return error("请求失败");
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.success = false;
        r.code = Resp.SUCCESS.code;
        r.message = msg;
        return r;
    }

    public static <T> R<T> error(Integer errCode) {
        R<T> r = new R<>();
        r.success = false;
        r.code = errCode;
        return r;
    }

    public static <T> R<T> error(Integer errCode, String msg) {
        R<T> r = new R<>();
        r.success = false;
        r.code = errCode;
        r.message = msg;
        return r;
    }

    public static <T> R<T> error(Resp codeE) {
        R<T> r = new R<>();
        r.success = false;
        r.code = codeE.code;
        r.message = codeE.message;
        return r;
    }
}
