package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Api(tags = "评论接口",description = " ")
@RestController
public class CommentController {

    @Resource
    CommentService commentService;

    @ApiOperation("用户评论")
    @PostMapping("/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "content", value = "评论内容", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse comment(@RequestParam("postId") Long postId
            , @RequestParam("content") String content, @RequestParam(value = "urls",required = false) List<String> urls,HttpServletRequest request) throws WebforumException {
        commentService.comment(postId, content, request,urls);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/uploadCommentImage")
    @ApiOperation("上传帖子图片(先上传图片获取到url，把获取到的url放到对应的段落，再调用评论)")
    public ApiRestResponse uploadCommentImage(@RequestParam(value = "images") List<MultipartFile> images, HttpServletRequest request) throws WebforumException {
        return commentService.uploadCommentImage(images,request);
    }

    @GetMapping("/getCommentNum")
    @ApiOperation("获取用户评论数量")
    public ApiRestResponse getUserPostNum(@RequestParam(value = "userId",required = false) Long userId,HttpServletRequest request) throws WebforumException {
        return commentService.getUserPostNum(userId,request);
    }

    @GetMapping("/getPostCommentNum")
    @ApiOperation("获取帖子评论数量")
    public ApiRestResponse getPostCommentNum(Long postId){
        return commentService.getPostCommentNum(postId);
    }


    @GetMapping("/selectComment")
    @ApiOperation("获取帖子评论内容")
    public ApiRestResponse selectCommentByParentId(@RequestParam Long postId,@RequestParam Integer pageNum,@RequestParam Integer pageSize) throws WebforumException {
        return ApiRestResponse.success(commentService.selectCommentByPostId(pageNum,pageSize,postId));
    }

    @PostMapping("/deleteComment")
    @ApiOperation("删除评论")
    public ApiRestResponse delComment(@RequestParam Long commentId,HttpServletRequest request) throws WebforumException {
        return commentService.deleteComment(commentId,request);
    }


    @PostMapping("/admin/rollbackComment")
    @ApiOperation("管理员恢复评论")
    public ApiRestResponse rollbackComment(@RequestParam Long commentId) throws WebforumException {
        return commentService.rollbackComment(commentId);
    }

    @GetMapping("/selectUserComment")
    @ApiOperation("通过Token或id查询用户评论")
    public ApiRestResponse selectUserComment(@RequestParam Integer pageNum,@RequestParam Integer pageSize,Long userId,HttpServletRequest request) throws WebforumException {
        return commentService.selectCommentByUserId(pageNum,pageSize,userId,request);
    }


}
