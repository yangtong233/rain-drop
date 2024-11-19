package org.raindrop.common.enums;

public enum ReqMethodE {
    GET(1, "GET"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE");

    //请求方法编号
    public final Integer code;
    //请求方法名称
    public final String methodName;

    ReqMethodE(Integer code, String methodName) {
        this.code = code;
        this.methodName = methodName;
    }
}
