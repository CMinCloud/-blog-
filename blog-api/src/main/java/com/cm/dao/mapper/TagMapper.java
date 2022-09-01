package com.cm.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cm.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
//  根据文章id查询对应的标签（包含多个标签）
    List<Tag> findTagesBytagIds(long ArticleId);
//    查询最热标签
    List<Tag> listHotTagsByLimits(int limit);
}
