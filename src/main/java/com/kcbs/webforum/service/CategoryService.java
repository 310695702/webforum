package com.kcbs.webforum.service;

import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface CategoryService {

    PageInfo getListForCategory(Integer pageNum, Integer pageSize, Long categoryId,String orderBy) throws WebforumException;

    ApiRestResponse addCategory(String categoryName, MultipartFile file, HttpServletRequest request) throws WebforumException;

    ApiRestResponse getCategory();

    PageInfo getListForRollBackCategory(Integer pageNum, Integer pageSize) throws WebforumException;

    PageInfo SearchList(Integer pageNum, Integer pageSize, String content) throws WebforumException;

    void updateCategoryNameById(Long categoryId, String categoryName) throws WebforumException;

    void updateCategoryImageById(HttpServletRequest request,Long categoryId, MultipartFile categoryImage) throws WebforumException;

    void updateCategoryRecommend(Long categoryId, int type) throws WebforumException;

    int deleteCategoryById(Long categoryId) throws WebforumException;
}
