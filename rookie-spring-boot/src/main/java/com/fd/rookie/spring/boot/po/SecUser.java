package com.fd.rookie.spring.boot.po;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecUser extends Po{
    private static final long serialVersionUID = -7190235330247040568L;

    private String userId;

    private String userName;

    private String password;
}
