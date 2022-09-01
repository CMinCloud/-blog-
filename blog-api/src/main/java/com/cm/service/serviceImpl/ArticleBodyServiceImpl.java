package com.cm.service.serviceImpl;

import com.cm.dao.mapper.ArticleBodyMapper;
import com.cm.pojo.ArticleBody;
import com.cm.service.ArticleBodyService;
import com.cm.vo.ArticleBodyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    /**
     * 返回文章体
     * @param bodyId
     * @return
     */
    @Override
    public ArticleBodyVo findArticleBody(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        BeanUtils.copyProperties(articleBody,articleBodyVo);
        return articleBodyVo;
    }
}
