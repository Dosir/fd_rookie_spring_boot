package com.fd.rookie.spring.boot.config.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = -3197391566147988370L;

    private int code;
    private String msg;

    public BusinessException(String msg) {
        super();
        this.msg = msg;
    }

    public BusinessException(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

}
