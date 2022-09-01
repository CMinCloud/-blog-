package com.cm.controller;


import com.cm.service.CategoryService;
import com.cm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result ListCategorys(){
        return categoryService.listCategorys();
    }

//    查询文章分类
    @GetMapping("/detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetails();
    }

//    查询单个分类详情
    @GetMapping("/detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long categoryId){
        return categoryService.categoriesDetailById(categoryId);
    }
}
