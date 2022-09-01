package com.cm.controller;

import com.cm.service.TagService;
import com.cm.vo.Result;
import com.cm.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result ListHotTags(){
//        设置最热标签的个数
        int limit = 6;
        List<TagVo> hotTags = tagService.hotTags(limit);
//        返回最热标签列表
        return Result.Success(hotTags);
    }

//    获取所有标签
    @GetMapping
    public Result ListTags(){
        return tagService.ListTags();
    }

    @GetMapping("/detail")
    public Result findAllTags(){
        return tagService.findAllTags();
    }

    @GetMapping("/detail/{id}")
    public Result findAllTags(@PathVariable("id") Long TagId){
        return tagService.findDetailById(TagId);
    }




}
