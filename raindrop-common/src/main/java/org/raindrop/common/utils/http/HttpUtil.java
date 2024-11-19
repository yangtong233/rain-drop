package org.raindrop.common.utils.http;

import cn.hutool.core.util.StrUtil;
import org.raindrop.common.enums.ReqMethodE;
import org.raindrop.common.exception.BaseException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送http请求的工具类
 */
public class HttpUtil {

    final static Pattern pattern;
    static {
        pattern = Pattern.compile("^(http://|https://)?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|localhost|.+\\..+\\..+)(:)?(\\d*)((/\\w*)*)$");
    }

    /**
     * 直接发送get请求
     * */
    public static <T> T get(String url, Map<String, String> heads, Map<String, String> param, Class<T> clazz) {
        return sendRequest(ReqMethodE.GET, "http", url, heads, param, null, clazz);
    }

    /**
     * 直接发送post请求
     * */
    public static <B, T> T post(String url, Map<String, String> heads, Map<String, String> param, B body, Class<T> clazz) {
        return sendRequest(ReqMethodE.POST, "http", url, heads, param, body, clazz);
    }

    /**
     * 发送请求
     * @param method  请求类型，默认GET
     * @param protocol 协议，默认http
     * @param ip    ip地址，默认localhost
     * @param port  端口，默认80
     * @param uri 请求资源路径
     * @param heads 请求头
     * @param param 参数集合
     * @param body 请求体数据
     * @param clazz 返回值类型
     * @return  转化成T类型的响应体数据
     */
    private static <B, T> T sendRequest(ReqMethodE method, String protocol, String ip, Integer port, String uri, Map<String, String> heads, Map<String, String> param, B body, Class<T> clazz) {
        RequestInfo requestInfo = RequestInfo.builder()
                .method(method)
                .protocol(protocol)
                .ip(ip)
                .port(port)
                .uri(uri)
                .param(param)
                .body(body)
                .header(heads).build();
        requestInfo.send();

        return requestInfo.get(clazz);
    }

    /**
     * 发送请求
     * @param method 请求类型
     * @param protocol  协议
     * @param url   请求地址，例如:127.0.0.1:8888/test
     * @param param 请求参数
     * @param heads 请求头
     * @param clazz 返回值类型
     * @return 转化成T类型的响应体数据
     */
    private static <B, T> T sendRequest(ReqMethodE method, String protocol, String url, Map<String, String> heads, Map<String, String> param, B body, Class<T> clazz) {
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            //从url中解析出协议(如果有的话)，ip，端口，和资源路径
            String ip = null;
            Integer port = null;
            String uri = null;
            for (int i = 1; i < matcher.groupCount(); i++) {
                if (i == 1 && StrUtil.isNotEmpty(matcher.group(i))) {
                    protocol = matcher.group(i);
                } else if (i == 2 && StrUtil.isNotEmpty(matcher.group(i))) {
                    ip = matcher.group(i);
                } else if (i == 4 && StrUtil.isNotEmpty(matcher.group(i))) {
                    port = Integer.valueOf(matcher.group(i));
                } else if (i == 5 && StrUtil.isNotEmpty(matcher.group(i))) {
                    uri = matcher.group(i);
                }
            }

            return sendRequest(method, protocol, ip, port, uri ,heads, param, body, clazz);
        } else {
            throw new BaseException("url格式不合法!");
        }
    }



    /**下面的方法都是使用中间类HttpProperty来发送请求的*/

    /**
     * 给请求添加请求头
     * @param key
     * @param value
     * @return
     */
    public static HttpProperty header(String key, String value) {
        HttpProperty property = new HttpProperty();
        property.header(key, value);
        return property;
    }

    /**
     * 给请求添加请求参数
     * @param key
     * @param value
     * @return
     */
    public static HttpProperty param(String key, String value) {
        HttpProperty property = new HttpProperty();
        property.param(key, value);
        return property;
    }

    /**
     * 给请求体添加数据
     * @param body
     * @param <T>
     * @return
     */
    public static <T> HttpProperty body(T body) {
        HttpProperty property = new HttpProperty();
        property.data(body);
        return property;
    }

    /**
     * 发起get请求
     * @param url
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T get(String url, Class<T> clazz) {
        HttpProperty property = new HttpProperty();
        return property.get(url, clazz);
    }

    /**
     * 发起post请求
     * @param url
     * @param clazz
     * @param <B>
     * @param <T>
     * @return
     */
    public static <B, T> T post(String url, Class<T> clazz) {
        HttpProperty property = new HttpProperty();
        return property.post(url, clazz);
    }

}
