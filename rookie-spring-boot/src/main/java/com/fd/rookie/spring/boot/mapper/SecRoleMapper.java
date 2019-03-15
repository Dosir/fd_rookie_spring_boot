package com.fd.rookie.spring.boot.mapper;

import com.fd.rookie.spring.boot.po.SecRole;

public interface SecRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(SecRole record);

    int insertSelective(SecRole record);

    SecRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(SecRole record);

    int updateByPrimaryKey(SecRole record);
}