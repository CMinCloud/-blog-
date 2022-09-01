package com.cm.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cm.dao.mapper.ArticleMapper;
import com.cm.dao.mapper.CommentMapper;
import com.cm.pojo.Comment;
import com.cm.service.ArticleService;
import com.cm.service.CommentService;
import com.cm.service.SysUserService;
import com.cm.utils.UserThreadLocal;
import com.cm.vo.CommentVo;
import com.cm.vo.LoginUserVo;
import com.cm.vo.Result;
import com.cm.vo.UserVo;
import com.cm.vo.param.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private ArticleService articleService;




    //    根据文章id查询评论列表
    @Override
    public Result CommentsByArticleId(Long articleId) {
//        1.根据文章 查询评论列表 ， 从comment表中查询
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
//        首先查询level为1的评论作为主体
        queryWrapper.eq(Comment::getLevel, 1).eq(Comment::getArticleId, articleId);
//        评论排序按时间倒序
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        // 获取封装后的Vo列表
        List<CommentVo> commentVoList = copyList(comments);

        return Result.Success(commentVoList);
    }

    /**
     * 发表评论,以comment的形式存入数据库
     *
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        Comment comment = new Comment();
//        1.获取评论的用户信息,直接调用ThreadLocal静态方法
        LoginUserVo user = UserThreadLocal.get();
        comment.setAuthorId(Long.valueOf(user.getId()));
        comment.setContent(commentParam.getContent());
        comment.setArticleId(commentParam.getArticleId());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            //说明该评论为level为1的评论
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
//        对parentId进行自主赋值,如果不存在则赋值为0
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        //对toUserId进行自主赋值,如果不存在则赋值为0
        comment.setToUid(toUserId == null ? 0 : toUserId);
//        新增评论
        commentMapper.insert(comment);
        // 评论发表成功data返回null即可
//        评论发表后，评论数+1
        articleService.commentCountsPlus(comment.getArticleId());
        return Result.Success(null);
    }

    /**
     * 对评论列表封装为vo类型
     *
     * @param comments
     * @return
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments
        ) {
//            对每一个comment对象封装为commentVo对象并填入VoList
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    /**
     * 单独封装每一个comment对象（包括评论的用户信息，子评论）
     *
     * @param comment
     * @return
     */
    private CommentVo copy(Comment comment) {
//        新建一个commentVo对象进行封装
        CommentVo commentVo = new CommentVo();
//        基础信息拷贝
        BeanUtils.copyProperties(comment, commentVo);
        UserVo userVo = userService.findUserVoById(comment.getAuthorId());
//        为评论vo添加用户信息
        commentVo.setAuthor(userVo);
        commentVo.setId(String.valueOf(comment.getId()));
//        添加子评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
//        手动添加评论日期，设置格式
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
//        如果该评论是子评论，设置被评论人对象
        if (comment.getLevel() > 1) {
            UserVo parentUser = userService.findUserVoById(comment.getToUid());
            commentVo.setToUser(parentUser);
        }
        return commentVo;
    }

    /**
     * 根据parentId查询对应的子评论列表
     *
     * @param ParentId
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long ParentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
//        子评论的level为2
        queryWrapper.eq(Comment::getLevel, 2).eq(Comment::getParentId, ParentId);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = new ArrayList<>();
//        对子评论也要进行封装为vo对象
        for (Comment comment : comments
        ) {
//            对每一个comment对象封装为commentVo对象并填入VoList
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }
}
