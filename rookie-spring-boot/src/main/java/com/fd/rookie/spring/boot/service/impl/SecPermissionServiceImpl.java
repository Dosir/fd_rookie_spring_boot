package com.fd.rookie.spring.boot.service.impl;

import com.fd.rookie.spring.boot.mapper.SecPermissionMapper;
import com.fd.rookie.spring.boot.po.SecPermission;
import com.fd.rookie.spring.boot.service.SecPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

public class SecPermissionServiceImpl implements SecPermissionService{
    @Autowired
    private SecPermissionMapper secPermissionMapper;

    @Override
    public SecPermission getSecPermission(String id) {
        return secPermissionMapper.selectByPrimaryKey(id);
    }
}
