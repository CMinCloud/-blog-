package com.cm.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cm.dao.mapper.TagMapper;
import com.cm.pojo.Category;
import com.cm.pojo.Tag;
import com.cm.service.TagService;
import com.cm.vo.Result;
import com.cm.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;


    //    将Tags类型列表转换为TagsVo类型
    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags
        ) {
//            注意这里要创建新的对象才行
            TagVo tagvo = new TagVo();
            tagvo.setId(String.valueOf(tag.getId()));
            tagvo.setTagName(tag.getTagName());
            tagVos.add(tagvo);
        }
//        返回转换成vo类型的tag
        return tagVos;
    }

//  根据ArticleId查询对应的标签
    @Override
    public List<TagVo> findTagsByArticleId(long articleId) {
//        调用mapper方法根据Aticleid查询对应标签
        List<Tag> tags = tagMapper.findTagesBytagIds(articleId);
//        将tags转换为tagVo并且赋值
        return copyList(tags);
    }


    /**
     * 查询最热标签
     *
     * @return
     */
    @Override
    public List<TagVo> hotTags(int limit) {
        List<Tag> tags = tagMapper.listHotTagsByLimits(limit);
        System.out.println(tags);
//        将tags转换为tagVo并且赋值
        return copyList(tags);
    }

//    查询所有标签
    @Override
    public Result ListTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
//        将tags转换为tagVo并且赋值
        return Result.Success(tags);
    }

//    查询标签页的所有   标签详情
    @Override
    public Result findAllTags() {
        List<Tag> tagList = tagMapper.selectList(null);
        return Result.Success(tagList);
    }

    @Override
    public Result findDetailById(Long tagId) {
        Tag tag = tagMapper.selectById(tagId);
        return Result.Success(tag);
    }
}
