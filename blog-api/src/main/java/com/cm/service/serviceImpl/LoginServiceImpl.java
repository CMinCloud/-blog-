package com.cm.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.cm.pojo.SysUser;
import com.cm.service.LoginService;
import com.cm.service.SysUserService;
import com.cm.utils.JWTUtils;
import com.cm.vo.ErrorCode;
import com.cm.vo.Result;
import com.cm.vo.param.LoginParam;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Transactional     //用于事务管理
public class LoginServiceImpl implements LoginService {

    //    设置一个加密盐
    private static final String salt = "cm!@#";

    @Autowired
    private SysUserService sysUserService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result login(LoginParam loginInfo) {
        String account = loginInfo.getAccount();
        String password = loginInfo.getPassword();

        if (StringUtils.isBlank(account) && StringUtils.isBlank(password)) {   //1.判断用户名和明码是否为空（参数名是哦福合法）
//            返回判断为空的错误结果
            return Result.Fail(ErrorCode.PARAM_ERROR.getCode(), ErrorCode.PARAM_ERROR.getMsg());
        }
//        2.通过md5对密码进行加密
        DigestUtils.md5(password + salt);
//        参数名合法：判断是否能够查询到
        SysUser user = sysUserService.findUser(account, password);
        if (user == null) {   //用户不存在，登陆失败
            return Result.Fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
//        3.登录成功，使用JWT生成token，返回token到redis中
        String token = JWTUtils.createToken(user.getId());
//        4.设置token信息（token，登录用户，过期时间）
        redisTemplate.opsForValue().set("TOKEN_" + token,
                JSON.toJSONString(user), 1, TimeUnit.DAYS);
//        5.返回成功的token信息
        return Result.Success(token);
    }

    @Override
    public Result logout(String token) {
//        删除redis库中对应token信息
        redisTemplate.delete("TOKEN_" + token);
        return Result.Success(null);
    }

    @Override
    public Result register(LoginParam registerInfo) {
        String account = registerInfo.getAccount();
        String password = registerInfo.getPassword();
        String nickname = registerInfo.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ) {
//            注册信息为空
            return Result.Fail(ErrorCode.PARAM_ERROR.getCode(), ErrorCode.PARAM_ERROR.getMsg());
        }
//        注册信息不为空，查询是否为已注册用户
        SysUser user = sysUserService.findUserByAccount(account);
        if (user != null) {    // 用户已存在
            return Result.Fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
//        新增未注册用户，封装用户信息
        user = new SysUser();
        user.setAccount(account);
        user.setNickname(nickname);
        user.setPassword(DigestUtils.md5Hex(password+salt));    //对密码进行加密
        user.setCreateDate(System.currentTimeMillis());
        user.setLastLogin(System.currentTimeMillis());
        user.setAvatar("/static/img/mqm.jpg");
        user.setAdmin(1); //1 为true
        user.setDeleted(0); // 0 为false
        sysUserService.save(user);

//        为该用户成成token并将token写入redis数据库，同时自动登录
        String token = JWTUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.Success(token);
    }


}
