package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Api(tags = "回复接口",description = " ")
@RestController
public class ReplyController {

    @Resource
    ReplyService replyService;

    //回复
    @PostMapping("/reply")
    @ApiOperation("回复")
    public ApiRestResponse reply(HttpServletRequest request,@RequestParam("commentId") Long commentId,
                                  @RequestParam("content") String content) throws WebforumException {
        replyService.reply(request,commentId,content);
        return ApiRestResponse.success();
    }

    //获取评论回复
    @GetMapping("/getReplyByCommentId")
    @ApiOperation("获取评论的回复")
    public ApiRestResponse getReplyByCommentId(@RequestParam("commentId") Long commentId,@RequestParam("pageNum") Integer pageNum
            ,@RequestParam("pageSize") Integer pageSize) throws WebforumException{
        return ApiRestResponse.success(replyService.selectReplyByCommentId(commentId,pageNum,pageSize));
    }


    //删除回复
    @DeleteMapping("/deleteReply")
    @ApiOperation("删除回复，管理员可删除所有，帖子发布者可删除整个帖子内回复，评论者可删除评论下所有回复，自己可删除自己回复")
    public ApiRestResponse deleteReply(@RequestParam("replyId") Long replyId,HttpServletRequest request) throws  WebforumException{
        replyService.deleteReply(replyId,request);
        return ApiRestResponse.success();
    }


    @GetMapping("/getUserReply")
    @ApiOperation("获取用户回复")
    public ApiRestResponse getUserReply(@RequestParam("userId") Long userId,@RequestParam Integer pageNum,@RequestParam Integer pageSize,HttpServletRequest request) throws WebforumException{
        return replyService.getUserReply(userId,pageNum,pageSize,request);
    }
}
