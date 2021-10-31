package com.kcbs.webforum.controller;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.model.pojo.Category;
import com.kcbs.webforum.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Api(tags = "帖子分类(查询、新增)接口",description = "获取所有帖子也在内")
@RestController
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/getPost")
    @ApiOperation("按分类查询帖子(不传则查询所有帖子)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, paramType = "query" ,dataType = "int"),
            @ApiImplicitParam(name = "categoryId", value = "分类id", required = false, paramType = "query",dataType = "long"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式，默认通过更新时间降叙排序，可传1(通过更新时间升叙排列),2(热帖降序),3(热帖升序),4(精华)", required = false, paramType = "query",dataType = "string")
    })
    public ApiRestResponse getListForCategory(Integer pageNum, Integer pageSize,Long categoryId,String orderBy) throws WebforumException {
        PageInfo pageInfo = categoryService.getListForCategory(pageNum, pageSize, categoryId,orderBy);
        return ApiRestResponse.success(pageInfo);
    }


    @GetMapping("/admin/getPost")
    @ApiOperation("查询回收站内所有帖子")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, paramType = "query" ,dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, paramType = "query",dataType = "int")
    })
    public ApiRestResponse getListForCategory(Integer pageNum, Integer pageSize) throws WebforumException {
        PageInfo pageInfo = categoryService.getListForRollBackCategory(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @GetMapping("/getCategory")
    @ApiOperation("获取所有分类")
    public ApiRestResponse getCategory(){
        return categoryService.getCategory();
    }

    @PostMapping("/admin/addCategory")
    @ApiOperation("管理员新增分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryName", value = "分类名称", required = true, paramType = "query", dataType = "String")
    })
    public ApiRestResponse addCategory(@RequestParam("categoryName") String categoryName, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws WebforumException {
        return categoryService.addCategory(categoryName,file,request);
    }


    @PostMapping("/admin/updateCategoryName")
    @ApiOperation("管理员修改分类名称")
    public ApiRestResponse updateCategoryName(@RequestParam("categoryId") Long categoryId,@RequestParam("categoryName") String categoryName) throws WebforumException {
        categoryService.updateCategoryNameById(categoryId,categoryName);
        return ApiRestResponse.success();
    }


    @PostMapping("/admin/updateCategoryImage")
    @ApiOperation("管理员修改分类图片")
    public ApiRestResponse updateCategoryImage(HttpServletRequest request,@RequestParam("categoryId") Long categoryId,@RequestParam("categoryImage")MultipartFile categoryImage) throws WebforumException {
        categoryService.updateCategoryImageById(request,categoryId,categoryImage);
        return ApiRestResponse.success();
    }


    @PostMapping("/admin/updateCategoryRecommend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId",value = "分类id",required = true,paramType = "query",dataType = "long"),
            @ApiImplicitParam(name = "type",value = "类型 0-取消推荐 1-推荐",required = true,paramType = "query",dataType = "int")
    })
    @ApiOperation("管理员修改分类推荐类型")
    public ApiRestResponse updateCategoryRecommend(@RequestParam("categoryId")Long categoryId,@RequestParam("type") int type) throws WebforumException{
        categoryService.updateCategoryRecommend(categoryId,type);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/deleteCategory")
    @ApiOperation("管理员删除分类")
    public ApiRestResponse deleteCategory(@RequestParam("categoryId")Long categoryId) throws WebforumException{
        int i = categoryService.deleteCategoryById(categoryId);
        String s = "删除了"+i+"个帖子";
        return ApiRestResponse.success(s);
    }

    @GetMapping("/searchByString")
    @ApiOperation("模糊查询与content相关内容")
    public ApiRestResponse searchByString (@RequestParam Integer pageNum,@RequestParam Integer pageSize,String content) throws WebforumException {
        PageInfo pageInfo = categoryService.SearchList(pageNum, pageSize, content);
        return ApiRestResponse.success(pageInfo);
    }

}
