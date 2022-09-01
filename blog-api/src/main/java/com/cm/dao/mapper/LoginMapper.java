package com.cm.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cm.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface LoginMapper extends BaseMapper<Article> {


}
