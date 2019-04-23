package com.fd.rookie.spring.boot.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 */
@Data
public class Result implements Serializable{
    private static final long serialVersionUID = 2891928503835708673L;

    private int code;
    private String message;
    private Object data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        return this;
    }
}
