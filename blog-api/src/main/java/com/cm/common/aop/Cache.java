package com.cm.common.aop;

//设置缓存aop注解，让浏览器从内存中获取资源从而大大提高读取效率

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    long expire() default  1*6* 1000;   //默认缓存1min

    String name() default  "";   //默认缓存名为""
}
