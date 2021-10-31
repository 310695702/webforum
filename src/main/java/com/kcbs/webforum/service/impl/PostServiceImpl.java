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
import com.kcbs.webforum.model.request.PostReq;
import com.kcbs.webforum.model.vo.PostVO;
import com.kcbs.webforum.service.PostService;
import com.kcbs.webforum.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class PostServiceImpl implements PostService {
    @Resource
    PostMapper postMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    PostImagesMapper postImagesMapper;
    @Resource
    MsgStateMapper msgStateMapper;
    @Resource
    UCSubscribeMapper ucSubscribeMapper;
    @Resource
    GoodMapper goodMapper;

    //发帖
    @Override
    public void post(PostReq postReq, HttpServletRequest request, List<String> urls) throws WebforumException {
        //获取用户信息
        ApiRestResponse res = JwtUtils.checkToken(request);
        User user = JSONObject.parseObject(res.getData().toString(), User.class);
        Post result = postMapper.selectByTitle(postReq.getTitle());
        //判断是否存在相同标题内容，如果已存在抛出异常
        if (result!=null){
            if (result.getUserId()==user.getUserId()&&result.getCategoryId()==postReq.getCategoryId()){
                throw new WebforumException(WebforumExceptionEnum.TITLE_EXISTED);
            }
        }
        //判断分类是否存在
        Category category = categoryMapper.selectByPrimaryKey(postReq.getCategoryId());
        if (category==null){
            throw new WebforumException(WebforumExceptionEnum.WRONG_CATEGORY);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postReq,post);
        post.setCreateTime(new Date(System.currentTimeMillis()));
        post.setUserId(user.getUserId());
        int count = postMapper.insertSelective(post);
        if (count!=1){
            throw new WebforumException(WebforumExceptionEnum.POST_FAILED);
        }
        if (urls!=null){
            long postid = postMapper.selectLastId();
            for (String s:urls){
                    PostImages postImages = new PostImages();
                    postImages.setPostId(postid);
                    postImages.setPostImage(s);
                    postImagesMapper.insertSelective(postImages);
            }
            Jedis jedis = null;
            try {
                jedis = new Jedis(Constant.HOST,Constant.PORT);
                if (jedis.exists(JwtUtils.getUser(request).getUserId()+"images")) {
                    jedis.del(JwtUtils.getUser(request).getUserId()+"images");
                }
            }catch (JedisConnectionException e){
                throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
            }finally {
                if (jedis!=null){
                    jedis.close();
                }
            }
        }
        //加经验
        ucSubscribeMapper.post(user.getUserId(),postReq.getCategoryId());
    }


    //删除帖子
    @Override
    public void deletepost(List<Long> postId,HttpServletRequest request) throws WebforumException {
        ApiRestResponse res = JwtUtils.checkToken(request);
        User user = JSONObject.parseObject(res.getData().toString(), User.class);
        List<Post> result = postMapper.selectListByPrimaryKey(postId);
        if (result.size()==0){
            throw new WebforumException(WebforumExceptionEnum.DELETE_FAILED);
        }
        for (Post post:result){
            if (post.getUserId()!=user.getUserId()){
                if (user.getRole()!=2){
                    throw new WebforumException(WebforumExceptionEnum.DELETE_FAILED);
                }
            }
            if (post.getVisibility()==2){
                throw new WebforumException(WebforumExceptionEnum.DELETE_FAILED);
            }
            post.setVisibility(2);
            int count = postMapper.updateByPrimaryKey(post);
            if (count!=1){
                throw new WebforumException(WebforumExceptionEnum.DELETE_FAILED);
            }
        }
    }


    @Override
    public void rollbackpost(List<Long> postId) throws WebforumException {
        List<Post> result = postMapper.selectListByPrimaryKey(postId);
        if (result.size()==0){
            throw new WebforumException(WebforumExceptionEnum.ROLLBACK_FAILED);
        }
        for (Post post:result){
            if (post.getVisibility()==1){
                throw new WebforumException(WebforumExceptionEnum.ROLLBACK_FAILED);
            }
            post.setVisibility(1);
            int count = postMapper.updateByPrimaryKey(post);
            if (count!=1){
                throw new WebforumException(WebforumExceptionEnum.ROLLBACK_FAILED);
            }
        }
    }

    @Override
    public ApiRestResponse getPostNum(Long userId, HttpServletRequest request) throws WebforumException {
        Long id = null;
        if (userId!=null&&!userId.equals("")){
            id = userId;
        }else {
            id = JwtUtils.getUser(request).getUserId();
        }
        return ApiRestResponse.success(postMapper.selectPostNumById(id));
    }

    @Override
    //@Cacheable(value = "selectByUserId")
    public ApiRestResponse selectByUserId(Integer pageNum, Integer pageSize, Long userId,HttpServletRequest request) throws WebforumException {
        Long id = checkpageNumAndpageSize(pageNum, pageSize, userId, request);
        PageHelper.startPage(pageNum,pageSize,"update_time desc");
        List<PostVO> posts = postMapper.selectByUserId(id);
        for (PostVO postVO :posts){
            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
            Good good = goodMapper.selectByPostId(postVO.getPostId());
            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                postVO.setGoodNum(list.size());}
        }
        PageInfo pageInfo = new PageInfo(posts);
        return ApiRestResponse.success(pageInfo);
    }

    @Override
    //@Cacheable(value = "getSubscribePost")
    public PageInfo getSubscribePost(Integer pageNum, Integer pageSize, HttpServletRequest request) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        PageHelper.startPage(pageNum,pageSize,"update_time desc");
        List<PostVO> postVOS = postMapper.selectSubscribePostList(JwtUtils.getUser(request).getUserId());
        for (PostVO postVO : postVOS){
            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
            Good good = goodMapper.selectByPostId(postVO.getPostId());
            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                postVO.setGoodNum(list.size());}
        }
        PageInfo pageInfo = new PageInfo(postVOS);
        return pageInfo;
    }

    @Override
    public ApiRestResponse getPostById(Long postId,HttpServletRequest request) throws WebforumException {
        if (postId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        User user = null;
        try {
            user = JwtUtils.getUser(request);
        }catch (Exception e){
            user = null;
        }
        if (user!=null){
            MsgState msgState = msgStateMapper.selectByDID(user.getUserId(), postId);
            if (msgState!=null){
                msgState.setMsgState(0);
                msgStateMapper.updateByPrimaryKeySelective(msgState);
            }
        }
        PostVO postVO = postMapper.selectById(postId);
        if (postVO==null){
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        Post post = new Post();
        post.setPostId(postVO.getPostId());
        post.setViewNum(postVO.getViewNum()+1);
        post.setUpdateTime(postVO.getUpdateTime());
        postMapper.updateByPrimaryKeySelective(post);
        postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
        Good good = goodMapper.selectByPostId(postVO.getPostId());
        if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
        postVO.setGoodNum(list.size());}
        return ApiRestResponse.success(postVO);
    }

    @Override
    public ApiRestResponse selectPostImages(Long postId) {
        List<String> list = postImagesMapper.selectPostImages(postId);
        return ApiRestResponse.success(list);
    }

    @Override
    public ApiRestResponse uploadPostImage(List<MultipartFile> images,HttpServletRequest request) throws WebforumException {
        if (images==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Jedis jedis = null;
        try{
            List<String> urls = new ArrayList<>();
            jedis = new Jedis(Constant.HOST,Constant.PORT);
            if (jedis.exists(JwtUtils.getUser(request).getUserId()+"images")) {
                List<String> jsonObject = JSONObject.parseArray(jedis.get(JwtUtils.getUser(request).getUserId() + "images"),String.class);
                for (String s:jsonObject){
                    File file2 = new File(Constant.POSTIMAGE_UPLOAD_DIR +s.substring(s.lastIndexOf("/")));
                    file2.delete();
                }
                jedis.del(JwtUtils.getUser(request).getUserId()+"images");
            }
            for (MultipartFile multipartFile:images){
                String newFileName = UserServiceImpl.createFileName(multipartFile);
                //创建文件夹
                File fileDirectory = new File(Constant.POSTIMAGE_UPLOAD_DIR);
                //目标文件
                File destFile = new File(Constant.POSTIMAGE_UPLOAD_DIR + newFileName);
                UserServiceImpl.addFile(multipartFile,fileDirectory,destFile);
                try {
                    String path = UserServiceImpl.getHost(new URI(request.getRequestURL() + "")) + "/PostImages/" + newFileName;
                    urls.add(path);
                }catch (URISyntaxException e) {
                    throw new WebforumException(WebforumExceptionEnum.POST_FAILED);
                }
            }
            jedis.set(JwtUtils.getUser(request).getUserId()+"images",JSONObject.toJSONString(urls));
            return ApiRestResponse.success(urls);
        }catch (JedisConnectionException e){
            throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }

    }

    @Override
    public ApiRestResponse addEssences(Long postId,int type) throws WebforumException {
        if (postId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post==null){
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        if (post.getIsEssences()==type){
            if (type==0){
                throw new WebforumException(WebforumExceptionEnum.CANCEL_ESSENCES_ERROR);
            }else if (type==1){
                throw new WebforumException(WebforumExceptionEnum.ADD_ESSENCES_ERROR);
            }

        }
        if (type==1||type==0){
            post.setIsEssences(type);
        }else {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        int i = postMapper.updateByPrimaryKeySelective(post);
        if (i==0){
            throw new WebforumException(WebforumExceptionEnum.ADD_ESSENCES_ERROR);
        }
        return ApiRestResponse.success();
    }


    //随机获取帖子
    @Override
    public ApiRestResponse selectRandomPost() {
        List<Long> postIds = postMapper.getAllPostId();
        List<PostVO> list = new ArrayList<>();
        if (postIds.size()<10){
            for (Long l:postIds){
                list.add(postMapper.selectById(l));
            }
            for (PostVO postVO : list){
                postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                Good good = goodMapper.selectByPostId(postVO.getPostId());
                if(good!=null){List<String> users = JSONObject.parseArray(good.getUsers(), String.class);
                    postVO.setGoodNum(users.size());}
            }
            return ApiRestResponse.success(list);
        }
        Random random = new Random();
        Set<Integer> ids = new HashSet<>();
        while (ids.size()<10){
            ids.add(random.nextInt(postIds.size()));
        }
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()){
            list.add(postMapper.selectById(postIds.get(iterator.next())));
        }
        for (PostVO postVO : list){
            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
            Good good = goodMapper.selectByPostId(postVO.getPostId());
            if(good!=null){List<String> users = JSONObject.parseArray(good.getUsers(), String.class);
                postVO.setGoodNum(users.size());}
        }
        return ApiRestResponse.success(list);
    }


    //检测分页参数
    public static Long checkpageNumAndpageSize(Integer pageNum, Integer pageSize, Long userId, HttpServletRequest request) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Long id = null;
        if (userId!=null){
            id = userId;
        }else {
            id = JwtUtils.getUser(request).getUserId();
        }
        return id;
    }
}
