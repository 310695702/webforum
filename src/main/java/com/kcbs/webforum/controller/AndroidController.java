package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.service.AndroidService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AndroidController {
    @Resource
    AndroidService androidService;

    @GetMapping("/GetVersion")
    @ResponseBody
    public ApiRestResponse getVersion(){
        return androidService.selectVersion();
    }


}
