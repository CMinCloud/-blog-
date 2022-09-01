package com.cm.service;

import com.cm.vo.ArticleVo;
import com.cm.vo.TagVo;
import com.cm.vo.param.PageParam;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class serviceTest {
    @Resource
    private ArticleService articleService;

    @Resource
    private TagService tagService;


    @Test
    void TagList(){
        List<TagVo> tagsByArticleId = tagService.findTagsByArticleId(1);
        tagsByArticleId.forEach(System.out::println);
    }

    @Test
    void ListHotTags(){
        List<TagVo> tagVos = tagService.hotTags(6);
        System.out.println(tagVos);
    }

    @Test
    void ListArchives(){
        List<HashMap<String,Object>> archives = articleService.ListArchives();
        System.out.println(archives);
    }
}
