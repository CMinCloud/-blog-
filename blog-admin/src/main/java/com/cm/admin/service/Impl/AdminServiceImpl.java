package com.cm.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cm.admin.mapper.AdminMapper;
import com.cm.admin.mapper.PermissionMapper;
import com.cm.admin.pojo.Admin;
import com.cm.admin.pojo.Permission;
import com.cm.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    public Admin findAdminByUserName(String username){
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
//        通过last字符，当匹配到一个时停止匹配
        queryWrapper.eq(Admin::getUsername,username).last("limit 1");
//        返回查询的用户结果
        return adminMapper.selectOne(queryWrapper);
    }

    //    根据管理员的id查询对应的permission列表
    @Override
    public List<Permission> finPermissionsByAdminId(Long id) {
        return permissionMapper.findPermissionsByAdminId(id);
    }
}
