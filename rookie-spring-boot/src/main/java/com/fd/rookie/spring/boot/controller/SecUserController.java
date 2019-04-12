package com.fd.rookie.spring.boot.controller;

import com.fd.rookie.spring.boot.po.SecUser;
import com.fd.rookie.spring.boot.service.SecUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secUser")
@Api(tags = "1.1", description = "用户管理", value = "用户管理")
public class SecUserController {
    @Autowired
    private SecUserService secUserService;

    @GetMapping("/{userId}")
    @ApiOperation(value = "主键查询（DONE）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
    })
    public SecUser getSecUserInfo(@PathVariable String userId) {
        return secUserService.getSecUserInfo(userId);
    }
}
