package com.fd.rookie.spring.boot.common;

/**
 * 响应码枚举类
 */
public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    TOO_MANY_REQUESTS(429),
    MODIFIED_BY_ANOHTER(430),
    INTERNAL_SERVER_ERROR(500),
    RPC_SERVICE_ERROR(501);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
