package com.cm.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cm.pojo.ArticleBody;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleBodyMapper extends BaseMapper<ArticleBody> {
}
