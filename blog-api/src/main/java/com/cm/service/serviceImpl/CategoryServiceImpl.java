package com.cm.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cm.dao.mapper.CategoryMapper;
import com.cm.pojo.Category;
import com.cm.service.CategoryService;
import com.cm.vo.CategoryVo;
import com.cm.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

//    根据类别id查询对应标签
    @Override
    public CategoryVo findCategory(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

//    查询所有标签
    @Override
    public Result listCategorys() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        查询必要字段:id和name
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category: categoryList
             ) {
//            直接将category列表转换为vo类型
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category,categoryVo);
            categoryVo.setId(String.valueOf(category.getId()));
            categoryVoList.add(categoryVo);
        }
        return Result.Success(categoryVoList);
    }

    @Override
    public Result findAllDetails() {
        // 查询所有
        List<Category> categoryList = categoryMapper.selectList(null);
        return Result.Success(categoryList);
    }

    @Override
    public Result categoriesDetailById(Long categoryId) {
//        查询目标分类的详情
        Category category = categoryMapper.selectById(categoryId);
        return Result.Success(category);
    }
}
