package com.kcbs.webforum.controller;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Api(tags = "轮播",description = " " )
@RestController
public class BannerController {
    @Resource
    BannerService bannerService;

    @PostMapping("/admin/addBanner")
    @ApiOperation("新增轮播")
    public ApiRestResponse addBanner(HttpServletRequest request, @RequestParam("postId") Long postId, @RequestParam("file") MultipartFile file) throws WebforumException {
        bannerService.addBanner(request, postId, file);
        return ApiRestResponse.success();
    }

    @GetMapping("/getBanner")
    @ApiOperation("获取轮播")
    public ApiRestResponse getBanner() {
        return bannerService.getBanner();
    }

    @DeleteMapping("/admin/deleteBanner")
    @ApiOperation("删除轮播")
    @ApiImplicitParam(name = "bannerId" ,value = "轮播id",required = true,type = "query",dataType = "long")
    public ApiRestResponse deleteBanner(@RequestParam Long bannerId) throws WebforumException {
        bannerService.deleteBanner(bannerId);
        return ApiRestResponse.success();
    }
}
