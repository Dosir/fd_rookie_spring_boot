package com.fd.rookie.spring.boot.mapper;

import com.fd.rookie.spring.boot.po.SecUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

public interface UserMapper extends Mapper<SecUser> {
    List<SecUser> findByUsername(@Param("userName") String userName);
}
