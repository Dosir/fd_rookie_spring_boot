package com.fd.rookie.spring.boot.service.impl;

import com.fd.rookie.spring.boot.mapper.UserMapper;
import com.fd.rookie.spring.boot.po.SecUser;
import com.fd.rookie.spring.boot.service.SecUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecUserServiceImpl implements SecUserService{
    @Autowired
    private UserMapper secUserMapper;

    @Override
    public SecUser getSecUserInfo(String userId) {
        return secUserMapper.selectByPrimaryKey(userId);
    }
}
