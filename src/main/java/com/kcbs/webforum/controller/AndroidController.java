package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.service.AndroidService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Api(tags = "安卓版本",description = " ")
@Controller
public class AndroidController {
    @Resource
    AndroidService androidService;

    @GetMapping("/GetVersion")
    @ResponseBody
    public ApiRestResponse getVersion() {
        return androidService.selectVersion();
    }


}
