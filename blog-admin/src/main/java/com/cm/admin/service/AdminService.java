package com.cm.admin.service;

import com.cm.admin.pojo.Admin;
import com.cm.admin.pojo.Permission;

import java.util.List;

public interface AdminService {

    Admin findAdminByUserName(String username);

//    根据管理员的id查询对应的permission列表
    List<Permission> finPermissionsByAdminId(Long id);
}
