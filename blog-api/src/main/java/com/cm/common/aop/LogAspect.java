package com.cm.common.aop;


import com.alibaba.fastjson.JSON;
import com.cm.utils.HttpContextUtils;
import com.cm.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

//定义aop切面
@Component
@Aspect
@Slf4j          //用来创建日志对象
public class LogAspect {

    //    定义切入点
//    @Pointcut("execution(Result com.cm.controller.ArticleController.listArticle())")   // 这种为标准形式，但需要设定每一个具体方法的地址
    @Pointcut("@annotation(com.cm.common.aop.LogAnnotation)")  //自定义注解，从而设置路径
    private void logPointCut() {
    }

    @Around("logPointCut()")
    public Object aroundListArticle(ProceedingJoinPoint pt) throws Throwable {
        long beginTime = System.currentTimeMillis();
//        执行方法
        Object result = pt.proceed();
        // 计算执行时间
        long time = System.currentTimeMillis() - beginTime;
//        保存日志
        recordLog(pt, time);
//        返回值为获取类型
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log start================================");
        log.info("module:{}", logAnnotation.module());
        log.info("operation:{}", logAnnotation.operation());

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}", className + "." + methodName + "()");

//        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}", params);

        //获取request 设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));


        log.info("excute time : {} ms", time);
        log.info("=====================log end================================");
    }
}

