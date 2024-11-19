package org.raindrop.common.utils.http;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.enums.ReqMethodE;
import org.raindrop.common.exception.BaseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求信息
 */
@Slf4j
public class RequestInfo {
    //请求方法类型
    private ReqMethodE method;
    //协议
    private String protocol;
    //ip
    private String ip;
    //端口
    private Integer port;
    //uri
    private String uri;
    //请求头集合
    private Map<String, String> heads;
    //请求参数
    private String param;
    //请求体数据
    private String body;

    //响应体数据
    private byte[] result;

    private RequestInfo(ReqMethodE method, String protocol, String ip, Integer port,
                        String uri, Map<String, String> heads, String param, String body) {
        this.method = method;
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
        this.uri = uri;
        this.heads = heads;
        this.param = param;
        this.body = body;
    }

    public static RequestInfoBuilder builder() {
        return new RequestInfoBuilder();
    }

    /**
     * 发送请求并以json方式读取返回请求体数据
     */
    public void send() {
        HttpURLConnection connection = null;
        try {
            //1.得到访问地址的URL
            String urlStr = StrUtil.format("{}://{}{}{}{}{}{}",
                    protocol,
                    ip,
                    port == null ? "" : ":",
                    port == null ? "" : port,
                    uri == null ? "" : uri,
                    StrUtil.isEmpty(param) ? "" : "?",
                    StrUtil.isEmpty(param) ? "" : URLEncodeUtil.encode(param, Charset.defaultCharset()));
            URL url = new URL(urlStr);
            //2.得到网络访问对象java.net.HttpURLConnection
            connection = (HttpURLConnection) url.openConnection();
            /*3.设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接 */
            //设置请求方式
            connection.setRequestMethod(method.methodName);
            //设置请求头
            if (heads != null) {
                for (Map.Entry<String, String> head : heads.entrySet()) {
                    connection.setRequestProperty(head.getKey(), head.getValue());
                }
            }
            //设置是否向HttpURLConnection输出
            connection.setDoOutput(true);
            //设置是否从HttpUrlConnection读入
            connection.setDoInput(true);
            //设置是否使用缓存
            connection.setUseCaches(true);
            //设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            connection.setInstanceFollowRedirects(true);
            //设置读取超时时间
            connection.setReadTimeout(300000);
            //设置连接超时时间
            connection.setConnectTimeout(300000);
            log.info("正在发送请求:{}", urlStr);
            //连接
            connection.connect();
            //4.得到响应状态码的返回值 responseCode
            int code = connection.getResponseCode();
            //仅当响应码为200，才返回响应体数据
            if (code == 200) {
                //如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                //从流中读取响应信息
                InputStream is = connection.getInputStream();
                result = IoUtil.readBytes(is);
                is.close();

                log.info("发送请求成功:{}", urlStr);
            } else {
                log.error("发送请求失败，响应状态码{}", code);
                throw new BaseException(StrUtil.format("发送请求失败，响应状态码{}", code));
            }

        } catch (IOException e) {
            log.error("发送请求失败，失败原因:{}", e.getMessage());
            throw new BaseException(StrUtil.format("发送请求失败，失败原因:{}", e.getMessage()));
        } finally {
            //6.断开连接，释放资源
            connection.disconnect();
        }
    }

    public <T> T get(Class<T> clazz) {
        if (result == null) {
            return null;
        }
        else if (clazz.equals(byte[].class)) {
            return (T)result;
        }
        else if (clazz.equals(String.class)) {
            return (T)new String(result);
        }
        T t = JSONUtil.toBean(new String(result), clazz);
        return t;
    }

    static class RequestInfoBuilder {
        //请求方法类型
        private ReqMethodE method = ReqMethodE.GET;
        //协议
        private String protocol = "http";
        //ip
        private String ip = "localhost";
        //端口
        private Integer port;
        //资源路径
        private String uri;
        //请求头集合
        private Map<String, String> heads;
        //请求参数
        private String param;
        //请求参数集合
        private Map<String, String> paramMap;
        //请求体数据
        private String body;

        public RequestInfoBuilder method(ReqMethodE method) {
            this.method = method;
            return this;
        }

        public RequestInfoBuilder protocol(String protocol) {
            this.protocol = protocol;
            if (StrUtil.isNotEmpty(this.protocol) && StrUtil.endWith(this.protocol, "://") && this.protocol.length() >= 3) {
                this.protocol = this.protocol.substring(0, this.protocol.length() - 3);
            }
            return this;
        }

        public RequestInfoBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public RequestInfoBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public RequestInfoBuilder uri(String uri) {
            if (StrUtil.isNotEmpty(uri) && !StrUtil.startWith(uri, "/")) {
                uri = "/" + uri;
            }
            this.uri = uri;
            return this;
        }

        public RequestInfoBuilder header(String key, String value) {
            if (this.heads == null) {
                this.heads = new HashMap<>();
            }
            this.heads.put(key, value);
            return this;
        }

        public RequestInfoBuilder header(Map<String, String> heads) {
            if (this.heads == null) {
                this.heads = new HashMap<>();
            }
            if (heads != null) {
                this.heads.putAll(heads);
            }
            return this;
        }

        public RequestInfoBuilder param(Map<String, String> paramMap) {
            if (paramMap != null) {
                StringBuilder params = new StringBuilder();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    params.append(param.getKey()).append("=").append(param.getValue()).append("&");
                }
                if (params.length() > 0 && '&' == params.charAt(params.length() - 1)) {
                    params = params.deleteCharAt(params.length() - 1);
                }
                this.param = params.toString();
            }

            return this;
        }

        public <T> RequestInfoBuilder body(T body) {
            return body(JSONUtil.toJsonStr(body));
        }

        public RequestInfoBuilder body(String body) {
            this.body = body;
            return this;
        }

        public RequestInfo build() {
            return new RequestInfo(method, protocol, ip, port, uri, heads, param, body);
        }

    }

}
