package com.cm.admin.controller;

import com.cm.admin.pojo.PageParam;
import com.cm.admin.pojo.Permission;
import com.cm.admin.service.PermissionService;
import com.cm.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

//    查询权限列表
    @PostMapping("/permission/permissionList")
    public Result PermissionList(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }

//    新增权限
    @PostMapping("/permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

//    修改权限信息
    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

//    删除权限
    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }



}
