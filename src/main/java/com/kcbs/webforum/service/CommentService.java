package com.kcbs.webforum.service;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface CommentService {
    void comment(Long postId, String content, HttpServletRequest request) throws WebforumException;

    ApiRestResponse getPostNum(Long userId, HttpServletRequest request) throws WebforumException;

    ApiRestResponse getPostCommentNum(Long postId);

    PageInfo selectCommentByPostId(Integer pageNum,Integer pageSize,Long postId) throws WebforumException;

    ApiRestResponse deleteComment(Long commentId,HttpServletRequest request) throws WebforumException;

    ApiRestResponse rollbackComment(Long commentId) throws WebforumException;

    ApiRestResponse selectCommentByUserId(Integer pageNum,Integer pageSize,Long userId, HttpServletRequest request) throws WebforumException;
}
