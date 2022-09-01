package com.cm.admin.service;

import com.cm.admin.pojo.PageParam;
import com.cm.admin.pojo.Permission;
import com.cm.admin.vo.Result;

public interface PermissionService {
//    返回权限控制列表
    Result listPermission(PageParam pageParam);
//    新增权限
    Result add(Permission permission);
//    修改权限
    Result update(Permission permission);
//    删除权限
    Result delete(Long id);
}
