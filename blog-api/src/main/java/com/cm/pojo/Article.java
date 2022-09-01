package com.cm.pojo;

import lombok.Data;

@Data
public class Article {
    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;
//    标题
    private String title;
//    简介,摘要
    private String summary;
//    评论数
    private Integer commentCounts;
//    浏览数（可根据浏览数筛选 最热文章）
    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 是否置顶，weight = 1 代表当前文章置顶
     */
    private Integer weight;


    /**
     * 创建时间
     */
    private Long createDate;
}
