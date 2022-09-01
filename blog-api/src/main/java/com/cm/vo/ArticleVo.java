package com.cm.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

//用来存放article库中和页面交互的数据
@Data
public class ArticleVo {
//    防止雪花算法精度丢失
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

//    如果按照标准需求，这里应该返回一个作者，而不是S听类型的作者名
    private String author;          // 作者

    private ArticleBodyVo body;

    private List<TagVo> tags;       // 标签

    private CategoryVo category;    //文章类别
}
