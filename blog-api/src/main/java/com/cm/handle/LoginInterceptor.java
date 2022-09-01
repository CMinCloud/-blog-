package com.cm.handle;

import com.alibaba.fastjson.JSON;
import com.cm.pojo.SysUser;
import com.cm.service.SysUserService;
import com.cm.utils.JWTUtils;
import com.cm.utils.UserThreadLocal;
import com.cm.vo.ErrorCode;
import com.cm.vo.LoginUserVo;
import com.cm.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/*
使用拦截器，进行登录拦截，如果遇到需要登录才能访问的接口，
如果未登录，拦截器直接返回，并跳转登录页面。
*/
//配置登录拦截器
@Component
@Slf4j        //   lombok 日志打印
public class LoginInterceptor implements HandlerInterceptor {


    @Autowired
    private SysUserService userService;
   /* 1.需要判断 请求的接口路径是否为 HandlerMethod(controller方法)
    2.判断token是否为空，如果为空 未登录
    3.如果token不为空，登录验证loginService checkToken
    4.如果认证成功 放行即可  */

    //    在方法执行前进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {   //请求的接口路径不是 HandlerMethod,放行
            return true;
        }
//        对handlerMethod进行拦截处理: 对token进行判空
        String token = request.getHeader("Authorization");
        // 日志打印
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        if (token == null) {
            Result result = Result.Fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
//            设置响应头
            response.setContentType("applicaton/json;charset=utf-8");
//            打印相应信息
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

//        token不空，对token进行校验
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            Result result = Result.Fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
//        登录验证成功，放行  ,并将用户信息存储到ThreadLocal中!!!
//        这一步是关键,之后就可以直接从ThreadLocal中get到用户信息
        Result result = userService.getUserInfoByToken(token);
        UserThreadLocal.put((LoginUserVo) result.getData());
        return true;
    }

  /*在方法完成后执行
  在获取完用户信息后清楚ThreadLocal缓存*/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
