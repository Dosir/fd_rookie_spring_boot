package com.fd.rookie.spring.boot.service;

import com.fd.rookie.spring.boot.po.SecPermission;
import org.springframework.stereotype.Service;

@Service
public interface SecPermissionService {

    SecPermission getSecPermission(String id);

}
