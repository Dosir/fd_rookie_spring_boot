package com.fd.rookie.spring.boot.mapper;

import com.fd.rookie.spring.boot.po.SecPermission;
import com.fd.rookie.spring.boot.utils.Page;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SecPermissionMapper extends Mapper<SecPermission>{
    List<SecPermission> getSecPermissionList(Page page);
}
