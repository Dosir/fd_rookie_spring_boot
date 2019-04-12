package com.fd.rookie.spring.boot.service;

import com.fd.rookie.spring.boot.po.SecUser;

public interface SecUserService {
    SecUser getSecUserInfo(String userId);
}
