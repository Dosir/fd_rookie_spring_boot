package com.fd.rookie.spring.boot.po;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@ApiModel
public class SecUser extends Po{
    private static final long serialVersionUID = -7190235330247040568L;

    @Id
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;
}
