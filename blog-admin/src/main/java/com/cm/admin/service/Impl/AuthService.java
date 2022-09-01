package com.cm.admin.service.Impl;

import com.cm.admin.pojo.Admin;
import com.cm.admin.pojo.Permission;
import com.cm.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication) {
//        获取请求路径
        String requestURI = request.getRequestURI();
        log.info("request url:{}", requestURI);
        //true代表放行 false 代表拦截
        Object principal = authentication.getPrincipal();   //代表获取到的管理员信息
        if (principal == null || "anonymousUser".equals(principal)) {
//            判断是否查询到管理员对象或者是匿名对象，则未登录
            return false;
        }
//        登录成功,获取用户登录账号名
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        Admin admin = adminService.findAdminByUserName(username);
        if (admin == null) return false;
        if (admin.getId() == 1) return true;  //id为1，是超级用户，直接放行

//        获取用户对应的权限
        System.out.println("admin:"+admin);
        List<Permission> permissions = adminService.finPermissionsByAdminId(admin.getId());
//        去掉可能的存在的？
        System.out.println(permissions);
        try{
            requestURI = StringUtils.split(requestURI,"?")[0];
        }catch (Exception e){
            System.out.println("抛出空指针异常:"+e.getMessage());
        }
        int i = 0;
        System.out.println(requestURI);
        for(Permission permission: permissions){
            System.out.println(++i +"/permission:"+permission);
            if(requestURI.equals(permission.getPath())){
//                对用户权限进行判断
                log.info("权限通过");
                return true;
            }
        }
        return false;
    }
}
