package com.cm.vo.param;

import lombok.Data;

@Data
public class CommentParam {
//    文章id
    private Long articleId;
//     内容
    private String content;
//      父评论
    private Long parent;
//      父评论对象id
    private Long toUserId;
}
