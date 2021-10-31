package com.kcbs.webforum.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.CommentMapper;
import com.kcbs.webforum.model.dao.PostMapper;
import com.kcbs.webforum.model.dao.ReplyMapper;
import com.kcbs.webforum.model.dao.UserMapper;
import com.kcbs.webforum.model.pojo.Comment;
import com.kcbs.webforum.model.pojo.Post;
import com.kcbs.webforum.model.pojo.Reply;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.vo.ReplyVO;
import com.kcbs.webforum.service.ReplyService;
import com.kcbs.webforum.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReplyServiceImpl implements ReplyService {

    @Resource
    CommentMapper commentMapper;
    @Resource
    ReplyMapper replyMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public void reply(HttpServletRequest request, Long commentId, String content) throws WebforumException {
        User user = JwtUtils.getUser(request);
        //如果参数为空 抛出异常
        if (commentId==null|| StringUtils.isEmpty(content)){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        //如果评论不存在 抛出异常
        if (comment==null){
            throw new WebforumException(WebforumExceptionEnum.COMMENT_EXISTED);
        }
        Post post = postMapper.selectByPrimaryKey(comment.getParentId());
        if (post==null){
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        Reply reply = new Reply();
        reply.setCommentId(commentId);
        reply.setUserId(user.getUserId());
        reply.setContent(content);
        int count = replyMapper.insertSelective(reply);
        //如果insert 0条数据 抛出异常
        if (count==0){
            throw new WebforumException(WebforumExceptionEnum.REPLY_FAILED);
        }
        //否则 通知用户 todo
    }

    @Override
    public PageInfo selectReplyByCommentId(Long commentId, Integer pageNum, Integer pageSize) throws WebforumException {
        if (commentId==null||pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        //如果评论为空 抛出异常
        if (comment==null){
            throw new WebforumException(WebforumExceptionEnum.COMMENT_EXISTED);
        }
        Post post = postMapper.selectByPrimaryKey(comment.getParentId());
        if (post==null){
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        PageHelper.startPage(pageNum,pageSize,"reply_time");
        List<ReplyVO> replyList =  replyMapper.selectByCommentId(commentId);
        PageInfo pageInfo = new PageInfo(replyList);
        return pageInfo;
    }

    @Override
    public void deleteReply(Long replyId,HttpServletRequest request) throws WebforumException {
        if (replyId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = JwtUtils.getUser(request);
        Reply reply = replyMapper.selectByPrimaryKey(replyId);
        if (reply==null){
            throw new WebforumException(WebforumExceptionEnum.REPLY_EXISTED);
        }
        //如果为普通用户
        if (user.getRole()==1){
            //如果就是回复者本人
            if (user.getUserId()==reply.getReplyId()){
                int count = replyMapper.deleteByPrimaryKey(replyId);
                //如果删除数量为0抛出异常
                if (count==0){
                    throw new WebforumException(WebforumExceptionEnum.DELETE_REPLY_FAILED);
                }
            }else {//否则查询是否为评论主人
                Comment comment = commentMapper.selectByPrimaryKey(reply.getCommentId());
                //如果评论为空 抛出异常
                if (comment==null){
                    throw new WebforumException(WebforumExceptionEnum.COMMENT_EXISTED);
                }
                //否则判断是否为评论主人,如果是 删除
                if (comment.getUserId()==user.getUserId()){
                    int count = replyMapper.deleteByPrimaryKey(replyId);
                    //如果删除数量为0抛出异常
                    if (count==0){
                        throw new WebforumException(WebforumExceptionEnum.DELETE_REPLY_FAILED);
                    }
                }else {//否则判断是否为帖子主人
                    Post post = postMapper.selectByPrimaryKey(comment.getParentId());
                    //如果帖子为空抛出异常
                    if (post==null){
                        throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
                    }
                    //如果是帖子主人,删除
                    if (post.getUserId()==user.getUserId()){
                        int count = replyMapper.deleteByPrimaryKey(replyId);
                        //如果删除数量为0抛出异常
                        if (count==0){
                            throw new WebforumException(WebforumExceptionEnum.DELETE_REPLY_FAILED);
                        }
                    }else {//否则抛出异常
                        throw new WebforumException(WebforumExceptionEnum.DELETE_REPLY_FAILED);
                    }
                }
            }
        }else {//为管理员用户
            int count = replyMapper.deleteByPrimaryKey(replyId);
            //如果删除数量为0抛出异常
            if (count==0){
                throw new WebforumException(WebforumExceptionEnum.DELETE_REPLY_FAILED);
            }
        }

    }

    @Override
    public ApiRestResponse getUserReply(Long userId, Integer pageNum, Integer pageSize, HttpServletRequest request) throws WebforumException {
        User user = null;
        if (userId==null){
            user = JwtUtils.getUser(request);
        }else {
            user = userMapper.selectByPrimaryKey(userId);
        }
        if (user==null){
            throw new WebforumException(WebforumExceptionEnum.USER_EXISTED);
        }
        PageHelper.startPage(pageNum,pageSize,"reply_time desc");
        List<ReplyVO> replyVOS = replyMapper.selectReplyByUser(user.getUserId());
        PageInfo pageInfo = new PageInfo(replyVOS);
        return ApiRestResponse.success(pageInfo);
    }


}
