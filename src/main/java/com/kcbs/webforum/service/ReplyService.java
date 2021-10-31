package com.kcbs.webforum.service;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;

import javax.servlet.http.HttpServletRequest;

public interface ReplyService {
    void reply(HttpServletRequest request, Long commentId, String content) throws WebforumException;

    PageInfo selectReplyByCommentId(Long commentId,Integer pageNum,Integer pageSize) throws WebforumException;

    void deleteReply(Long replyId,HttpServletRequest request) throws WebforumException;

    ApiRestResponse getUserReply(Long userId, Integer pageNum, Integer pageSize, HttpServletRequest request) throws WebforumException;
}
