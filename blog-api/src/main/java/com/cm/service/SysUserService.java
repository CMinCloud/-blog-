package com.cm.service;

import com.cm.pojo.SysUser;
import com.cm.vo.Result;
import com.cm.vo.UserVo;

public interface SysUserService {
    SysUser findUserById(Long AuthorId);

    SysUser findUser(String account, String password);

    UserVo findUserVoById(Long AuthorId);

    Result getUserInfoByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser user);


}
