package com.kcbs.webforum.controller;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.model.request.PostReq;
import com.kcbs.webforum.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "帖子接口",description = " ")
@RestController
public class PostController {
    @Resource
    PostService postService;

    @ApiOperation("用户发帖")
    @PostMapping("/user/post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "categoryId", value = "分类id", required = true, paramType = "query",dataType = "long")
    })
    public ApiRestResponse post(@Valid PostReq postReq, @RequestParam(value = "urls",required = false) List<String> urls, HttpServletRequest request) throws WebforumException {
        postService.post(postReq,request,urls);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/uploadPostImage")
    @ApiOperation("上传帖子图片(先上传图片获取到url，把获取到的url放到对应的段落，再调用发帖)")
    public ApiRestResponse uploadPostImage( @RequestParam(value = "images") List<MultipartFile> images,HttpServletRequest request) throws WebforumException {
        return postService.uploadPostImage(images,request);
    }

    @ApiOperation("用户或管理员删除帖子")
    @PostMapping("/deletePost")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, paramType = "query", dataType = "Long" ,allowMultiple = true),
    })
    public ApiRestResponse deletepost(@RequestParam List<Long> postId, HttpServletRequest request) throws WebforumException {
        postService.deletepost(postId,request);
        return ApiRestResponse.success();
    }

    @ApiOperation("管理员恢复帖子")
    @PostMapping("/admin/rollBackPost")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, paramType = "query", dataType = "Long" ,allowMultiple = true),
    })
    public ApiRestResponse rollbackpost(@RequestParam List<Long> postId) throws WebforumException {
        postService.rollbackpost(postId);
        return ApiRestResponse.success();
    }

    @GetMapping("/getPostNum")
    @ApiOperation("获取帖子数量")
    public ApiRestResponse getPostNum(@RequestParam(value = "userId",required = false) Long userId,HttpServletRequest request) throws WebforumException {
        return postService.getPostNum(userId,request);
    }

    @GetMapping("/getPostByUserId")
    @ApiOperation("根据userId获取贴子")
    public ApiRestResponse getPostById(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize,@RequestParam(value = "userId",required = false) Long userId,HttpServletRequest request)throws WebforumException{
        return postService.selectByUserId(pageNum,pageSize,userId,request);
    }


    @GetMapping("/getSubscribePost")
    @ApiOperation("获取关注的人从关注后发的帖子")
    public ApiRestResponse getSubscribePost(@RequestParam Integer pageNum,@RequestParam Integer pageSize,HttpServletRequest request) throws WebforumException {
        PageInfo pageInfo =postService.getSubscribePost(pageNum,pageSize,request);
        return ApiRestResponse.success(pageInfo);
    }


    @GetMapping("/getPostById")
    @ApiOperation("通过PostId获取帖子内容")
    public ApiRestResponse getPostById(@RequestParam Long postId,HttpServletRequest request) throws WebforumException {
        return postService.getPostById(postId,request);
    }

    @GetMapping("/getPostImages")
    @ApiOperation("获取帖子图片")
    public ApiRestResponse getPostImages(@RequestParam Long postId){
        return postService.selectPostImages(postId);
    }

    @PostMapping("/admin/addEssences")
    @ApiOperation("帖子加精/取消加精")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "type", value = "类型 0-取消加精 1-加精", required = true, paramType = "query", dataType = "int"),
    })
    public ApiRestResponse addEssences(@RequestParam Long postId,@RequestParam int type) throws WebforumException {
        return postService.addEssences(postId,type);
    }
}
