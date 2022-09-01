package com.cm.service;

import com.cm.pojo.Article;
import com.cm.vo.ArticleVo;
import com.cm.vo.param.ArticleParam;
import com.cm.vo.param.PageParam;
import com.cm.vo.Result;

import java.util.HashMap;
import java.util.List;

public interface ArticleService {
//      List<ArticleVo> listArticle(PageParam pageParam);

//      展示最热文章，直接使用article实体类匹配数据
    List<ArticleVo> hotArticles(int limit);

    List<ArticleVo> newArticles(int limit);

    List<HashMap<String,Object>> ListArchives();
//      根据articleId查询对应的文章详情
    ArticleVo findArticleById(Long id);

    Result publish(ArticleParam articleParam);

//    评论数+1
    void commentCountsPlus(Long articleId);

    Result listArticle(PageParam pageParam);

    Result searchArticles(String search);
}
