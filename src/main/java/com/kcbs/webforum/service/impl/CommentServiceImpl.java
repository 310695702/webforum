package com.kcbs.webforum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.*;
import com.kcbs.webforum.model.pojo.*;
import com.kcbs.webforum.model.vo.CommentVo;
import com.kcbs.webforum.service.CommentService;
import com.kcbs.webforum.utils.JwtUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    @Resource
    ReplyMapper replyMapper;
    @Resource
    CommentImagesMapper commentImagesMapper;
    @Resource
    UCSubscribeMapper ucSubscribeMapper;

    @Override
    public void comment(Long postId, String content, HttpServletRequest request,List<String> urls) throws WebforumException {
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
        if (post==null){
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        ucSubscribeMapper.comment(user.getUserId(),post.getCategoryId());
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

        if (urls!=null){
            long commentId = commentMapper.selectLastId();
            String jsoncommentImages = JSONObject.toJSONString(urls);
            CommentImages commentImages = new CommentImages();
            commentImages.setCommentId(commentId);
            commentImages.setCommentImage(jsoncommentImages);
            commentImagesMapper.insertSelective(commentImages);
            Jedis jedis = null;
            try {
                jedis = new Jedis(Constant.HOST,Constant.PORT);
                if (jedis.exists(JwtUtils.getUser(request).getUserId()+"commentImages")) {
                    jedis.del(JwtUtils.getUser(request).getUserId()+"commentImages");
                }
            }catch (JedisConnectionException e){
                throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
            }finally {
                if (jedis!=null){
                    jedis.close();
                }
            }
        }

    }

    //获取用户评论数量
    @Override
    public ApiRestResponse getUserPostNum(Long userId, HttpServletRequest request) throws WebforumException {
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
        for (CommentVo commentVo:commentVos){
            commentVo.setReplyNum(replyMapper.getNumByCommentId(commentVo.getCommentId()));
                String commentImages =commentImagesMapper.selectByCommentId(commentVo.getCommentId());
            List<String> images = JSONObject.parseArray(commentImages,String.class);
            commentVo.setImages(images);
        }
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
        Post post = postMapper.selectAllByPrimaryKey(comment.getParentId());
        if (post==null){
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        if (comment.getUserId()!=user.getUserId()&&user.getRole()!=2&&user.getUserId()!=post.getUserId()){
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
    //@Cacheable(value = "selectCommentByUserId")
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

    @Override
    public ApiRestResponse uploadCommentImage(List<MultipartFile> images, HttpServletRequest request) throws WebforumException {
        if (images==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Jedis jedis = null;
        try{
            List<String> urls = new ArrayList<>();
            jedis = new Jedis(Constant.HOST,Constant.PORT);
            if (jedis.exists(JwtUtils.getUser(request).getUserId()+"commentImages")) {
                List<String> jsonObject = JSONObject.parseArray(jedis.get(JwtUtils.getUser(request).getUserId() + "commentImages"),String.class);
                for (String s:jsonObject){
                    File file2 = new File(Constant.COMMENTIMAGE_UPLOAD_DIR +s.substring(s.lastIndexOf("/")));
                    file2.delete();
                }
                jedis.del(JwtUtils.getUser(request).getUserId()+"commentImages");
            }
            for (MultipartFile multipartFile:images){
                String newFileName = UserServiceImpl.createFileName(multipartFile);
                //创建文件夹
                File fileDirectory = new File(Constant.COMMENTIMAGE_UPLOAD_DIR);
                //目标文件
                File destFile = new File(Constant.COMMENTIMAGE_UPLOAD_DIR + newFileName);
                UserServiceImpl.addFile(multipartFile,fileDirectory,destFile);
                try {
                    String path = UserServiceImpl.getHost(new URI(request.getRequestURL() + "")) + "/CommentImages/" + newFileName;
                    urls.add(path);
                }catch (URISyntaxException e) {
                    throw new WebforumException(WebforumExceptionEnum.COMMENT_FAILED);
                }
            }
            jedis.set(JwtUtils.getUser(request).getUserId()+"commentImages",JSONObject.toJSONString(urls));
            return ApiRestResponse.success(urls);
        }catch (JedisConnectionException e){
            throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }

    }
}
