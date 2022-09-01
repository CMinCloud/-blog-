package com.cm.service;

import com.cm.dao.mapper.LoginMapper;
import com.cm.vo.Result;
import com.cm.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;

public interface LoginService {
/*    1.检查参数是否合法

    2.根据用户名和密码查询用户表

    2.1用户不存在，登陆失败

    2.2用户存在，使用jwt 生成token 返回给前端

    3.token放入redis中， redis   token: user信息  设置过期时间*/
    Result login(LoginParam loginInfo);

    Result logout(String token);

    Result register(LoginParam registerInfo);
}
