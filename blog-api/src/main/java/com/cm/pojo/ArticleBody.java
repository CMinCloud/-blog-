package com.cm.pojo;

import lombok.Data;

@Data
public class ArticleBody {
    private Long id;        // BodyId
    private String content; //文章内容
    private String contentHtml;     // 文章html
    private Long articleId;     //所属文章id
}
