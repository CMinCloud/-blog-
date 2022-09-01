package com.cm.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cm.dao.mapper.ArticleMapper;
import com.cm.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    //                          使用自动注入填入参数mapper
    @Async("taskExecutor")       // 注意配置这个Bean注释
    public void updateViewCount(ArticleMapper articleMapper, Article article){
        Article articleUpdate = new Article();
//        重新创建一个article对象并赋值修改,这样只会修改目标参数
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        LambdaUpdateWrapper<Article> luw = new LambdaUpdateWrapper<>();
        luw.eq(Article::getId,article.getId());
//        类似乐观锁
        luw.eq(Article::getViewCounts,article.getViewCounts());
//        进行更新操作
        articleMapper.update(articleUpdate,luw);
        //线程睡眠2秒，不会影响主线程的使用
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
