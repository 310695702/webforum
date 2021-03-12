package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.service.SubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Api(tags = "用户关注接口",description = " ")
@RestController
public class SubscribeController {
    @Resource
    SubscribeService subscribeService;

    @PutMapping("/subscribe")
    @ApiOperation("关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beSubscribe", value = "被关注人id", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse subscribe(@RequestParam("beSubscribe") Long beSubscribe, HttpServletRequest request) throws WebforumException {
        return subscribeService.subscribe(beSubscribe,request);
    }

    @DeleteMapping("/subscribe")
    @ApiOperation("取消关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beSubscribe", value = "被关注人id", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse cancelsubscribe(@RequestParam("beSubscribe") Long beSubscribe, HttpServletRequest request) throws WebforumException {
        return subscribeService.cancelsubscribe(beSubscribe,request);
    }


    @GetMapping("/user/checkSubscribe")
    @ApiOperation("检查token用户是否已关注该用户")
    public ApiRestResponse checkSubscribe(@RequestParam("userId") Long userId,HttpServletRequest request) throws WebforumException {
        return subscribeService.checkSubscribeById(userId,request);
    }
}
