package com.cm.utils;

//使用ThreadLocal保存用户信息，并在使用完后释放

import com.cm.pojo.SysUser;
import com.cm.vo.LoginUserVo;

public class UserThreadLocal {

    private UserThreadLocal(){}

//    设置一个全局local变量来存储
    private static final ThreadLocal<LoginUserVo> LOCAL = new ThreadLocal<>();
//  存储用户
    public static void put(LoginUserVo user){
        LOCAL.set(user);
    }
//  获取用户
    public static LoginUserVo get(){
        return LOCAL.get();
    }
//    清楚本地缓存
    public static void remove(){
        LOCAL.remove();
    }

}
