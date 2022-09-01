package com.cm.vo.param;

import com.cm.vo.CategoryVo;
import com.cm.vo.TagVo;
import lombok.Data;

import java.util.List;

//  文章发布请求参数
@Data
public class ArticleParam {

    private String title;           //文章标题

    private Long id;                // 文章id

    private ArticleBodyParam body;  // 文章内容

    private CategoryVo category;    // 文章类别

    private String summary;     //文章概述

    private List<TagVo> tags;   //文章标签

    private String search;      //用于查询的参数
}
