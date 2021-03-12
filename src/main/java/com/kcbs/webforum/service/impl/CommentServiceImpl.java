package com.kcbs.webforum.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.CommentMapper;
import com.kcbs.webforum.model.dao.MsgStateMapper;
import com.kcbs.webforum.model.dao.PostMapper;
import com.kcbs.webforum.model.pojo.Comment;
import com.kcbs.webforum.model.pojo.MsgState;
import com.kcbs.webforum.model.pojo.Post;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.vo.CommentVo;
import com.kcbs.webforum.service.CommentService;
import com.kcbs.webforum.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService {
    @Resource
    CommentMapper commentMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    MsgStateMapper msgStateMapper;

    @Override
    public void comment(Long postId, String content, HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        if (StringUtils.isEmpty(content)||content.length()<5) {
            throw new WebforumException(WebforumExceptionEnum.WRONG_CONTENT);
        }
        Comment comment = new Comment();
        comment.setUserId(user.getUserId());
        comment.setContent(content);
        comment.setParentId(postId);
        comment.setCommentTime(new Date(System.currentTimeMillis()));
        comment.setVisibility(1);
        int count = commentMapper.insertSelective(comment);
        if (count!=1){
            throw new WebforumException(WebforumExceptionEnum.COMMENT_FAILED);
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        post.setUpdateTime(new Date(System.currentTimeMillis()));
        int ii = postMapper.updateByPrimaryKeySelective(post);
        if (ii==0){
            throw new WebforumException(WebforumExceptionEnum.COMMENT_FAILED);
        }
        MsgState state = msgStateMapper.selectByDID(post.getUserId(),postId);
        if (state==null){
            MsgState msgState = new MsgState();
            msgState.setUserId(post.getUserId());
            msgState.setPostId(postId);
            msgState.setMsgState(1);
            int i = msgStateMapper.insertSelective(msgState);
            if (i!=1){
                throw new WebforumException(WebforumExceptionEnum.COMMENT_FAILED);
            }
        }else {
            state.setMsgState(1);
            int i = msgStateMapper.updateByPrimaryKeySelective(state);
            if (i!=1){
                throw new WebforumException(WebforumExceptionEnum.COMMENT_FAILED);
            }
        }

    }

    @Override
    public ApiRestResponse getPostNum(Long userId, HttpServletRequest request) throws WebforumException {
        Long id = null;
        if (userId==null||userId.equals("")){
            id = JwtUtils.getUser(request).getUserId();
        }else {
            id = userId;
        }
        return ApiRestResponse.success(commentMapper.getCommentNumById(id));
    }


    //获取帖子评论数量
    @Override
    public ApiRestResponse getPostCommentNum(Long postId) {
        return ApiRestResponse.success(commentMapper.selectByPostId(postId));
    }

    @Override
    public PageInfo selectCommentByPostId(Integer pageNum,Integer pageSize ,Long postId) throws WebforumException {
        if (postId==null||pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        PageHelper.startPage(pageNum,pageSize,"comment_time asc");
        List<CommentVo> commentVos = commentMapper.selectByParentId(postId);
        PageInfo pageInfo = new PageInfo(commentVos);
        return pageInfo;
    }

    @Override
    public ApiRestResponse deleteComment(Long commentId, HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        if (commentId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment.getUserId()!=user.getUserId()&&user.getRole()!=2&&user.getUserId()!=postMapper.selectByPrimaryKey(comment.getParentId()).getUserId()){
            throw new WebforumException(WebforumExceptionEnum.DELETE_FAILED);
        }
        comment.setVisibility(2);
        int i = commentMapper.updateByPrimaryKeySelective(comment);
        if (i == 1){
            return ApiRestResponse.success();
        }
        return ApiRestResponse.error(WebforumExceptionEnum.DELETE_FAILED);
    }

    @Override
    public ApiRestResponse rollbackComment(Long commentId) throws WebforumException {
        if (commentId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        comment.setVisibility(1);
        int i = commentMapper.updateByPrimaryKeySelective(comment);
        if (i==1){
            return ApiRestResponse.success();
        }
        return ApiRestResponse.error(WebforumExceptionEnum.ROLLBACK_FAILED);
    }


    //通过用户ID获取用户评论
    @Override
    public ApiRestResponse selectCommentByUserId(Integer pageNum,Integer pageSize,Long userId, HttpServletRequest request) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Long id = null;
        if (userId!=null){
            id = userId;
        }else {
            id = JwtUtils.getUser(request).getUserId();
        }
        PageHelper.startPage(pageNum,pageSize,"comment_time desc");
        List<CommentVo> commentVos = commentMapper.selectByUserId(id);
        PageInfo pageInfo = new PageInfo(commentVos);
        return ApiRestResponse.success(pageInfo);
    }
}
