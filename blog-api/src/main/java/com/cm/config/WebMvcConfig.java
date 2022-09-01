package com.cm.config;

import com.cm.handle.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
        //本地测试 端口不一致 也算跨域
        registry.addMapping("/**").allowedOrigins("http://localhost:8080","http://47.113.193.238");
    }

    //    配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置登录拦截器,设置拦截路径为loginController中的test方法
        registry.addInterceptor(loginInterceptor).addPathPatterns("/login/test");
//        发表评论时设置拦截器,如果未登录则无法评论
        registry.addInterceptor(loginInterceptor).addPathPatterns("/comments/create/change");
//        发布文章时设置拦截器
        registry.addInterceptor(loginInterceptor).addPathPatterns("/articles/publish");
    }

//    访问配置的相应路径下的静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        对static静态目录下的资源放行
//        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
