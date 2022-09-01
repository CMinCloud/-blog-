package com.cm.controller;


import com.cm.pojo.SysUser;
import com.cm.service.LoginService;
import com.cm.utils.UserThreadLocal;
import com.cm.vo.LoginUserVo;
import com.cm.vo.Result;
import com.cm.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping           //解析json请求参数为实体类
    public Result login(@RequestBody LoginParam loginInfo) {
        return loginService.login(loginInfo);
    }

    @GetMapping("/test")
    public Result test() {
        LoginUserVo user = UserThreadLocal.get();
        System.out.println(user);
        return Result.Success(null);
    }

}
