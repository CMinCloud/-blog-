package com.cm.controller;

import com.cm.service.SysUserService;
import com.cm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/currentUser")
//    指定参数为RequestHeader中的Authorization
    public Result currentUser(@RequestHeader("Authorization") String token){
//        对token进行校验,校验成功后即登录成功
        return userService.getUserInfoByToken(token);
    }
}
