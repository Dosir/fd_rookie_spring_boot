package com.fd.rookie.spring.boot.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "sec_permission")
public class SecPermission extends Po{
    private static final long serialVersionUID = -979238470871093420L;

    @Id
    private Integer permissionId;

    private String url;

    private Integer parentId;

    private String description;

    private String permissionName;

}
