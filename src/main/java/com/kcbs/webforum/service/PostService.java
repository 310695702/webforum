package com.kcbs.webforum.service;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.request.PostReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
    void post(PostReq postReq, HttpServletRequest request,List<String> urls) throws WebforumException;

    void deletepost(List<Long> postId, HttpServletRequest request) throws WebforumException;

    void rollbackpost(List<Long> postId) throws WebforumException;

    ApiRestResponse getPostNum(Long userId, HttpServletRequest request) throws WebforumException;

    ApiRestResponse selectByUserId(Integer pageNum, Integer pageSize, Long userId,HttpServletRequest request) throws WebforumException;

    PageInfo getSubscribePost(Integer pageNum, Integer pageSize, HttpServletRequest request) throws WebforumException;

    ApiRestResponse getPostById(Long postId,HttpServletRequest request) throws WebforumException;

    ApiRestResponse selectPostImages(Long postId);

    ApiRestResponse uploadPostImage(List<MultipartFile> images,HttpServletRequest request) throws WebforumException;

    ApiRestResponse addEssences(Long postId,int type) throws WebforumException;
}
