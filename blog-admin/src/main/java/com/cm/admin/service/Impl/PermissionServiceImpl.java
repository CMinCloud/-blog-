package com.cm.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cm.admin.mapper.PermissionMapper;
import com.cm.admin.pojo.PageParam;
import com.cm.admin.pojo.Permission;
import com.cm.admin.service.PermissionService;
import com.cm.admin.vo.PageResult;
import com.cm.admin.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public Result listPermission(PageParam pageParam) {
        //        获取访问参数    当前页,页面最大显示数
        Integer currentPage = pageParam.getCurrentPage();
        Integer pageSize = pageParam.getPageSize();
        //      创建Page对象作为查询条件
        IPage<Permission> page = new Page<>(currentPage, pageSize);

        IPage<Permission> iPage = permissionMapper.selectPage(page, null);
//        必须从查询出来的page中才能够获取页面数据的相关信息! ! !

//        删除最后一页数据回显bug解决:  如果当前页码大于总页码数，则将当前页设置为最大页码
        if(currentPage > iPage.getPages()){
//            修改查询页码数
            page.setCurrent((int)iPage.getPages());
        }
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
//        对查询条件进行判定
        if(StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Permission::getName,pageParam.getQueryString());
        }
//        获取查询结果
        IPage<Permission> permissionPage = permissionMapper.selectPage(page, queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
//        填充进List
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());

        return Result.Success(pageResult);
    }

    @Override
    public Result add(Permission permission) {
//        直接进行插入操作
        permissionMapper.insert(permission);
        return Result.Success(null);
    }

    @Override
    public Result update(Permission permission) {
//        直接进行修改操作
        permissionMapper.updateById(permission);
        return Result.Success(null);
    }

    @Override
    public Result delete(Long id) {
        permissionMapper.deleteById(id);
        return Result.Success(null);
    }
}
