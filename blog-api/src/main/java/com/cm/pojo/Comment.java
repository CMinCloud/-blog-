package com.cm.pojo;

import lombok.Data;

@Data
public class Comment {

    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId;
//  父评论id
    private Long parentId;

    private Long toUid;
//    注意需要是封装类Integer。保证更新时会自带null
    private Integer level;
}
