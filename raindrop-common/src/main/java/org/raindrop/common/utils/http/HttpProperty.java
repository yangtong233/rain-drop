package org.raindrop.common.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送http请求的中间类，负责保存请求头信息,请求参数信息和请求体信息
 * 该类存在的意义是为了优雅的链式调用
 */
public class HttpProperty {
    private Map<String, String> headers = null;
    private Map<String, String> params = null;
    private Object body = null;

    public HttpProperty header(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public HttpProperty param(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public <B> HttpProperty data(B body) {
        this.body = body;
        return this;
    }

    public <T> T get(String url, Class<T> clazz) {
        return HttpUtil.get(url, headers, params, clazz);
    }

    public <B, T> T post(String url, Class<T> clazz) {
        return HttpUtil.post(url, headers, params, body, clazz);
    }

}
