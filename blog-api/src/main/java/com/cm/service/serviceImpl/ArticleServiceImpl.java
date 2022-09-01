package com.cm.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cm.dao.mapper.ArticleBodyMapper;
import com.cm.dao.mapper.ArticleMapper;
import com.cm.dao.mapper.ArticleTagMapper;
import com.cm.pojo.Article;
import com.cm.pojo.ArticleBody;
import com.cm.pojo.ArticleTag;
import com.cm.pojo.SysUser;
import com.cm.service.*;
import com.cm.utils.UserThreadLocal;
import com.cm.vo.*;
import com.cm.vo.param.ArticleParam;
import com.cm.vo.param.PageParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private ArticleBodyService articleBodyService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private ThreadService threadService;    //线程池的使用

    @Autowired
    private ArticleBodyMapper articleBodyMapper;   //设置文章体，操作ms_article_body表

    @Autowired
    private ArticleTagMapper articleTagMapper;     // 设置文章标签，建立article与tag的联系

    /**
     * 分页查询article数据库表
     *
     * @param pageParam
     * @return
     */
//    public List<ArticleVo> listArticle(PageParam pageParam) {
////        设置一个page对象，用来实施分页查询； 参数：页号，页展示数
//        Page<Article> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
//
//        LambdaQueryWrapper<Article> lbw = new LambdaQueryWrapper<>();
////        获取分类和标签参数
//        Long categoryId = pageParam.getCategoryId();
//        Long tagId = pageParam.getTagId();
////        获取归档信息参数
//        String year = pageParam.getYear();
//        String month = pageParam.getMonth();
//
//        if (categoryId != null) {
////            匹配对应类别的文章
//            lbw.eq(Article::getCategoryId, categoryId);
//        }
//        if (tagId != null) {
//            List<Long> articleIdList = new ArrayList<>();
////            匹配对应标签的文章
//            LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(ArticleTag::getTagId, tagId);
////            匹配对应的  TagId
//            List<ArticleTag> articleTags = articleTagMapper.selectList(queryWrapper);
//            for (ArticleTag articleTag : articleTags
//            ) {
////                找出对应的每一个articleId并存入List ，作为之后根据articleId筛选article的条件
//                articleIdList.add(articleTag.getArticleId());
//                if(articleIdList.size() > 0 ){
////                    查询以下articleId的article
//                    lbw.in(Article::getId,articleIdList);
//                }
//            }
//        }
////        匹配对应日期的归档文章
//        if(year != null && month != null){
////          这里mybatis-Plus无法处理毫秒时间的转换（或者说很麻烦，效率很低），因此采用sql语句吧
//        }
//        // LambdaQueryWrapper设置查询条件：根据指定条件 / 根据文章创建时间倒叙排列
//        lbw.orderByDesc(Article::getWeight, Article::getCreateDate);
////        该查询结果会存放在page中
//        Page<Article> articlePage = articleMapper.selectPage(page, lbw);
//        //获取page中的数据
//        List<Article> pageRecords = articlePage.getRecords();
////        返回article的vo对象,默认查询标签和作者信息
//        return copyList(pageRecords, true, true);
//    }

//    public List<ArticleVo> listArticle(PageParam pageParam) {
////        设置一个page对象，用来实施分页查询； 参数：页号，页展示数
//        Page<Article> Page = new Page<>(pageParam.getPage(),pageParam.getPageSize());
//    }



    /**
     * 首页-最热文章
     *
     * @param limit
     * @return
     */
    @Override
    public List<ArticleVo> hotArticles(int limit) {

        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
//        根据浏览数排序,最大查询数限制为limit,注意字符串拼接的间隔！！！
        qw.orderByDesc(Article::getViewCounts).last("limit " + limit);
//        进行条件查询
        qw.select(Article::getId, Article::getTitle);
        List<Article> Articles = articleMapper.selectList(qw);
        //        默认不包含作者和标签信息
        return copyList(Articles, false, false);
    }

    /**
     * 首页-最新文章
     *
     * @param limit
     * @return
     */
    @Override
    public List<ArticleVo> newArticles(int limit) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
//        根据创建时间排序,最大查询数限制为limit,注意字符串拼接的间隔！！！
        qw.orderByDesc(Article::getCreateDate).last("limit " + limit);
//        进行条件查询
        qw.select(Article::getId, Article::getTitle);
        List<Article> Articles = articleMapper.selectList(qw);
        //        默认不包含作者和标签信息
        return copyList(Articles, false, false);
    }

    /**
     * 查询 首页-归档文章信息（时间，数量）
     *
     * @return
     */
    @Override
    public List<HashMap<String, Object>> ListArchives() {
        return articleMapper.ListArchives();
    }

    /**
     * 根据ArticleId查询文章详情
     * 详情内容包括：articleBody，category
     *
     * @param id
     * @return
     */
    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);

//        在线程池中进行更新CountView操作，并且不会影响到主线程
        threadService.updateViewCount(articleMapper, article);
//        返回ArticleVo对象
        return copy(article, true, true, true, true);
    }

    /**
     * 发布文章(修改 ms_article表，ms_article_body表，ms_article_tag表)
     *
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {

//修改 ms_article表
        Article article = new Article();
//        属性填充

        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(Article.Article_Common);
        article.setViewCounts(Article.Article_Common);
//        获取当前登录用户的id
        article.setAuthorId(Long.valueOf(UserThreadLocal.get().getId()));
//        设置ArticleBody的id,暂时为一个空值，在body表创建完成后重新update一下就好
        article.setBodyId(-1L);
        article.setCategoryId(Long.valueOf(articleParam.getCategory().getId()));
        article.setWeight(0);           // 不进行置顶
        article.setCreateDate(System.currentTimeMillis());
        articleMapper.insert(article);    // 新增article，会自动 生成id（在下列方法中就可以直接getId()  ）

// ms_article_body表
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());      // 使用刚刚新建的article自主生成的id,和article表建立联系
        // 新增articleBody，会自动 生成id（在下列方法中就可以直接getId()  ）
        articleBodyMapper.insert(articleBody);

/**
 *   创建完成body后重新修改
 */
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

// ms_article_tag表
        List<TagVo> tags = articleParam.getTags();
        for (TagVo tagVo : tags
        ) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(Long.valueOf(tagVo.getId()));
//           新增articleTag表的内容
            articleTagMapper.insert(articleTag);
        }


        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
//        将成功生成的articleId存入vo对象中，并返回给前端代表文章发表成功
        return Result.Success(articleVo);

    }

    @Override
    public void commentCountsPlus(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        article.setCommentCounts(article.getCommentCounts() + 1);
//        更新评论数
        articleMapper.updateById(article);
    }

    @Override
    public Result listArticle(PageParam pageParam) {
//        创建page对象
        Page<Article> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParam.getCategoryId(), pageParam.getTagId(),
                pageParam.getYear(), pageParam.getMonth());
//        返回查询结果（查询列表，不包括单个文章详情，所以body不需要设置）
        return Result.Success(copyList(articleIPage.getRecords(),true,true));
    }

//    使用模糊查询调用搜索框
    @Override
    public Result searchArticles(String title) {
        //  设置MP的查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        按访问数进行排序
        queryWrapper.orderByDesc(Article::getViewCounts);
//        根据title查询articleId和title，前端会使用组件请求查询文章
        queryWrapper.select(Article::getId,Article::getTitle).like(Article::getTitle,title);
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.Success(copyList(articleList,false,false));


//        返回查询结果（查询列表，不包括单个文章详情，所以body不需要设置）

    }

    /**
     * 用来将数据列表转换成vo数据列表（vo是用来和页面交互的数据）
     * 首页-文章列表
     *
     * @param pageRecords
     * @return
     */
    private List<ArticleVo> copyList(List<Article> pageRecords, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : pageRecords
        ) {
//            将每一项转换成功的数据存入vo数据列表   (tag和author暂时默认为true)
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
/*            ArticleVo articleVo = new ArticleVo();
//            将每一项转化能成功的数据存入vo数据列表：  原始数据 -> 目标数据类型
            BeanUtils.copyProperties(record,articleVo);
//            为vo对象添加不能够直接覆盖的属性（如属性类型不一致）
            articleVo.setCreateDate(new DateTime(record.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            articleVoList.add(articleVo);*/
        }
        return articleVoList;
    }

    /**
     * CopyList，用来转换文章详情的Copy
     * 文章详情
     *
     * @param pageRecords
     * @param isTag
     * @param isAuthor
     * @param isBody
     * @param isCategory
     * @return
     */
    private List<ArticleVo> copyList(List<Article> pageRecords, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : pageRecords
        ) {
//            将每一项转换成功的数据存入vo数据列表   (tag和author暂时默认为true)
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
/*            ArticleVo articleVo = new ArticleVo();
//            将每一项转化能成功的数据存入vo数据列表：  原始数据 -> 目标数据类型
            BeanUtils.copyProperties(record,articleVo);
//            为vo对象添加不能够直接覆盖的属性（如属性类型不一致）
            articleVo.setCreateDate(new DateTime(record.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            articleVoList.add(articleVo);*/
        }
        return articleVoList;
    }

    /**
     * 将单项数据转换成vo对象
     *
     * @param record
     * @return
     */
    private ArticleVo copy(Article record, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(record.getId()));
//        使用Spring内置方法转换相同数据(没有覆盖的不能转换)
        BeanUtils.copyProperties(record, articleVo);
//        为vo对象添加不能够直接覆盖的属性（如属性类型不一致）
        articleVo.setCreateDate(new DateTime(record.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
//        并不是所有的接口都需要标签,作者信息
        if (isTag) {
            Long ArticleId = record.getId();
//            标签有多个
            articleVo.setTags(tagService.findTagsByArticleId(ArticleId));
        }
        if (isAuthor) {
//            作者只有一个，为某一用户
            SysUser author = userService.findUserById(record.getAuthorId());
//            获取用户（作者）的昵称
            articleVo.setAuthor(author.getNickname());
        }

//        只有文章体需要展示body和category内容
        if (isBody) {
            Long bodyId = record.getBodyId();
            ArticleBodyVo articleBody = articleBodyService.findArticleBody(bodyId);
            articleVo.setBody(articleBody);
        }
        if (isCategory) {
            Long categoryId = record.getCategoryId();
            CategoryVo category = categoryService.findCategory(categoryId);
            articleVo.setCategory(category);
        }
        return articleVo;
    }
}
