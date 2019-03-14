package com.fd.rookie.spring.boot.mapper;

import com.fd.rookie.spring.boot.po.SecUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<SecUser> findByUsername(@Param("userName") String userName);
}
