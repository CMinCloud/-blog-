package com.cm.controller;


import com.cm.service.CommentService;
import com.cm.vo.CommentVo;
import com.cm.vo.Result;
import com.cm.vo.param.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
         return  commentService.CommentsByArticleId(articleId);
    }

//    发表评论
    @PostMapping("/create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}
