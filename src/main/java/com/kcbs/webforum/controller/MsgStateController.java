package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.service.MsgStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户消息状态",description = "")
@RestController
public class MsgStateController {
    @Resource
    MsgStateService msgStateService;

    @GetMapping("/user/state")
    @ApiOperation("检查是否存在新消息，使用轮询(3S请求一次)")
    public ApiRestResponse CheckState(HttpServletRequest request) throws WebforumException {
        return msgStateService.checkState(request);
    }

    @GetMapping("/user/list")
    @ApiOperation("分页查询新回复")
    public ApiRestResponse list(HttpServletRequest request,Integer pageNum,Integer pageSize) throws WebforumException {
        return msgStateService.list(request,pageNum,pageSize);
    }
}
