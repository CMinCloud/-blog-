package com.cm.service;

import com.cm.vo.Result;
import com.cm.vo.param.CommentParam;

public interface CommentService {

//    根据ArticleId查询评论列表
    Result CommentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}
