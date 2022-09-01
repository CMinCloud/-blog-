package com.cm.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cm.dao.mapper.SysUserMapper;
import com.cm.pojo.SysUser;
import com.cm.service.SysUserService;
import com.cm.utils.JWTUtils;
import com.cm.vo.ErrorCode;
import com.cm.vo.LoginUserVo;
import com.cm.vo.Result;
import com.cm.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static javafx.scene.input.KeyCode.L;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser findUserById(Long AuthorId) {
        return userMapper.selectById(AuthorId);
    }

//    根据用户id返回用户vo对象
    public UserVo findUserVoById(Long AuthorId) {
        UserVo userVo = new UserVo();
        SysUser user = userMapper.selectById(AuthorId);
        BeanUtils.copyProperties(user,userVo);
//        注意id类型不匹配,要手动注入
        userVo.setId(String.valueOf(user.getId()));
        return userVo;
    }

    //    根据账号密码查询用户
    @Override
    public SysUser findUser(String account, String password) {

        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper();
//        设置匹配条件，匹配账号密码
        qw.eq(SysUser::getAccount, account).eq(SysUser::getPassword, password);
//        选择需要查询的字段
        qw.select(SysUser::getId, SysUser::getAccount,
                SysUser::getAvatar, SysUser::getNickname);
//        保证查询完成后停止，提高查询效率
        qw.last("limit 1");
        SysUser user = userMapper.selectOne(qw);
        return user;
    }

    //    根据token密钥查询用户信息
    @Override
    public Result getUserInfoByToken(String token) {
//        检查token密钥
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
//            登录失败
            return Result.Fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
//        密钥校验成功，从redis中匹配token信息并转换为json字符串,记得要和login阶段的存token格式一致
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
//            token密钥不同
            return Result.Fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
//            获取json字符串中的用户信息并封装为user对象，指定类型为SysUser
        SysUser user = JSON.parseObject(userJson, SysUser.class);
        //            对vo对象进行封装,考虑BeanUtils.copyProperties(xx,xx)
        LoginUserVo loginUserVo = new LoginUserVo();
//        记得将user中long类型中的id以String类型赋值给LoginUserVo
        BeanUtils.copyProperties(user,loginUserVo);
        loginUserVo.setId(String.valueOf(user.getId()));
//        loginUserVo.setAccount(user.getAccount());
//        loginUserVo.setAvatar(user.getAvatar());
//        loginUserVo.setId(user.getId());
//        loginUserVo.setNickname(user.getNickname());
        return Result.Success(loginUserVo);

    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
//        匹配account
        qw.eq(SysUser::getAccount,account);
        qw.last("limit 1");
//        返回查询出的user结果
        return userMapper.selectOne(qw);
    }

    @Override
    public void save(SysUser user) {
        //注意 默认生成的id 是分布式id 采用了雪花算法
        userMapper.insert(user);
    }


}
