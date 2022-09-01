package com.cm.vo.param;


import lombok.Data;

//          设置登录参数，redis配置，统一错误码
@Data
public class LoginParam {

//    账号
    private String account;
//    密码
    private String password;

//   昵称
    private String nickname;
}
