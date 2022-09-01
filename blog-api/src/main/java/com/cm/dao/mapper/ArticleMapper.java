package com.cm.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cm.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

//    首页-查询文章的归档信息
    List<HashMap<String,Object>> ListArchives();

    IPage<Article> listArticle(Page<Article> page,Long categoryId,Long tagId,
                               String year,String month);
}
