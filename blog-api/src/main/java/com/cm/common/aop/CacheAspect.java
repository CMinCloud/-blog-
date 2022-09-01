package com.cm.common.aop;

import com.alibaba.fastjson.JSON;
import com.cm.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;

@Aspect      //定义为aop
@Component  // 设置为Spring管理的事务
@Slf4j    // 开启日志
public class CacheAspect {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.cm.common.aop.Cache)")       //设置切入点
    public void cachePointCut() {
    }           //切入点模板,代表要追加功能的方法

    @Around("cachePointCut()")                  //设置环绕
    public Object around(ProceedingJoinPoint pjp) {
        try {
            //        获取对象签名
            Signature signature = pjp.getSignature();
//        类名
            String className = pjp.getTarget().getClass().getSimpleName();
//        方法名
            String methodName = signature.getName();
//        设置类数组
            Class[] parameterTypes = new Class[pjp.getArgs().length];

            Object[] args = pjp.getArgs();
//        参数
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNoneEmpty(params)) {
                //加密 以防出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
//            缓存过期时间
            long expire = annotation.expire();
//            缓存名称
            String name = annotation.name();

//            先尝试从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNoneEmpty(redisValue)) {
                log.info("走了缓存~~~,{},{}", className, methodName);

//                获取缓存中的值
                return JSON.parseObject(redisValue, Result.class);
            }
            Object proceed = pjp.proceed();
//              将对应key值的数据存入redis缓存(已存储过则相当于刷新),expire为存储时间
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~~{},{}",className,methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.Fail(-999,"系统错误");
    }
}
