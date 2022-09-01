package com.cm.service;

import com.cm.pojo.Tag;
import com.cm.vo.Result;
import com.cm.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(long articleId);

    List<TagVo> hotTags(int limit);

    Result ListTags();

    Result findAllTags();

    Result findDetailById(Long tagId);
}
