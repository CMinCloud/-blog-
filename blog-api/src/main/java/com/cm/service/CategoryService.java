package com.cm.service;

import com.cm.vo.CategoryVo;
import com.cm.vo.Result;

public interface CategoryService {

    CategoryVo findCategory(Long categoryId);

    Result listCategorys();
//    查询文章分类
    Result findAllDetails();

//    查询单个分类详情
    Result categoriesDetailById(Long categoryId);
}
