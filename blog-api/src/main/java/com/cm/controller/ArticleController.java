package com.cm.controller;

import com.cm.common.aop.Cache;
import com.cm.common.aop.LogAnnotation;
import com.cm.pojo.Article;
import com.cm.service.ArticleService;
import com.cm.vo.ArticleVo;
import com.cm.vo.param.ArticleParam;
import com.cm.vo.param.PageParam;
import com.cm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//json数据进行交互
@RestController
@RequestMapping("/articles")            // 这里路径设置为articles也可以
public class ArticleController {

    @Autowired
    private ArticleService articleService;

//加上注解，对此接口记录日志
    @LogAnnotation(module = "首页",operation = "获取文章列表")
    @Cache(expire = 5*60*1000,name = "获取文章列表listArticle")
    //    首页-文章列表
    @PostMapping
    public Result listArticle(@RequestBody PageParam pageParam) {
//        返回成功值
        return articleService.listArticle(pageParam);
    }

    //    首页-最热文章
    @PostMapping("/hot")
    @Cache(expire = 5*60*1000,name = "hot_article")
    public Result hotArticles() {
        int limit = 5;  //最热文章 最大显示数
        List<ArticleVo> hotArticles = articleService.hotArticles(limit);
//        默认不包含作者和标签信息
        return Result.Success(hotArticles);
    }

    @PostMapping("/new")
    public Result newArticles() {
        int limit = 5; //最新文章 最大显示书
        List<ArticleVo> newArticles = articleService.newArticles(limit);
        return Result.Success(newArticles);
    }

    //  文章归档
    @PostMapping("/listArchives")
    public Result listArchives() {
        return Result.Success(articleService.ListArchives());
    }

//      获取文章详情
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.Success(articleVo);
    }

//    查询文章
    @PostMapping("/search")
    public Result search(@RequestBody ArticleParam articleParam){
        String search = articleParam.getSearch();
        return Result.Success(articleService.searchArticles(search));
    }


//    发布文章
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}


