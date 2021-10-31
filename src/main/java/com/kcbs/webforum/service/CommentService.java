package com.kcbs.webforum.service;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {
    void comment(Long postId, String content, HttpServletRequest request,List<String> urls) throws WebforumException;

    ApiRestResponse getUserPostNum(Long userId, HttpServletRequest request) throws WebforumException;

    ApiRestResponse getPostCommentNum(Long postId);

    PageInfo selectCommentByPostId(Integer pageNum,Integer pageSize,Long postId) throws WebforumException;

    ApiRestResponse deleteComment(Long commentId,HttpServletRequest request) throws WebforumException;

    ApiRestResponse rollbackComment(Long commentId) throws WebforumException;

    ApiRestResponse selectCommentByUserId(Integer pageNum,Integer pageSize,Long userId, HttpServletRequest request) throws WebforumException;

    ApiRestResponse uploadCommentImage(List<MultipartFile> images, HttpServletRequest request) throws WebforumException;
}
