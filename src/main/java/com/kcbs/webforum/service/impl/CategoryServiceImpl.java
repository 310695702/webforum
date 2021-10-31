package com.kcbs.webforum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.CategoryMapper;
import com.kcbs.webforum.model.dao.GoodMapper;
import com.kcbs.webforum.model.dao.PostMapper;
import com.kcbs.webforum.model.pojo.Category;
import com.kcbs.webforum.model.pojo.Good;
import com.kcbs.webforum.model.pojo.Post;
import com.kcbs.webforum.model.vo.CategoryVO;
import com.kcbs.webforum.model.vo.PostVO;
import com.kcbs.webforum.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    GoodMapper goodMapper;

    //分类查询帖子
    @Override
    public PageInfo getListForCategory(Integer pageNum, Integer pageSize, Long categoryId,String orderBy) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }

        String order = "update_time desc";
        if (!StringUtils.isEmpty(orderBy)){
            if ((Integer.parseInt(orderBy)%2)==1){
                order = "update_time asc";
            }
        }

        if (categoryId==null){
            if (orderBy!=null){
                switch (order){
                    case "4":
                        PageHelper.startPage(pageNum,pageSize,order);
                        List<PostVO> posts = categoryMapper.selectEssencesList();
                        return getPageInfo(pageNum, pageSize, orderBy, order, posts);
                }
            }
                PageHelper.startPage(pageNum,pageSize,order);
                List<PostVO> posts = categoryMapper.selectList();
                return getPageInfo(pageNum, pageSize, orderBy, order, posts);

        }

        else {
            PageHelper.startPage(pageNum,pageSize,order);
            List<PostVO> posts = categoryMapper.selectListByCategory(categoryId);
            for (PostVO postVO :posts){
                postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                Good good = goodMapper.selectByPostId(postVO.getPostId());
                if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
            }
            if (orderBy!=null){
                switch (orderBy) {
                    case "1":
                        break;
                    case "2":
                        PageHelper.startPage(pageNum,pageSize,order);
                        List<PostVO> posts1 = categoryMapper.selectListByCategoryOrderByHot(categoryId);
                        for (PostVO postVO :posts1){
                            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                            Good good = goodMapper.selectByPostId(postVO.getPostId());
                            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
                        }
                        PageInfo pageInfo = new PageInfo(posts1);
                        return pageInfo;
                    case "3":
                        PageHelper.startPage(pageNum,pageSize,order);
                        List<PostVO> posts2 = categoryMapper.selectListByCategoryOrderByHot(categoryId);
                        for (PostVO postVO :posts2){
                            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                            Good good = goodMapper.selectByPostId(postVO.getPostId());
                            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
                        }
                        PageInfo pageInfo2 = new PageInfo(posts2);
                        return pageInfo2;
                    case "4":
                        PageHelper.startPage(pageNum,pageSize,order);
                        List<PostVO> posts3 = categoryMapper.selectEssencesListByCategoryId(categoryId);
                        for (PostVO postVO :posts3){
                            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                            Good good = goodMapper.selectByPostId(postVO.getPostId());
                            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
                        }
                        PageInfo pageInfo3 = new PageInfo(posts3);
                        return pageInfo3;
                }
            }
            PageInfo pageInfo = new PageInfo(posts);
            return pageInfo;
        }

    }

    private PageInfo getPageInfo(Integer pageNum, Integer pageSize, String orderBy, String order, List<PostVO> posts) {
        for (PostVO postVO :posts){
            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
            Good good = goodMapper.selectByPostId(postVO.getPostId());
            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
        }
        if (orderBy!=null){
            switch (orderBy) {
                case "1":
                    break;
                case "2":
                    PageHelper.startPage(pageNum,pageSize,order);
                    List<PostVO> posts1 = categoryMapper.selectListOrderByHot();
                    for (PostVO postVO :posts1){
                        postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                        Good good = goodMapper.selectByPostId(postVO.getPostId());
                        if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
                    }
                    PageInfo pageInfo = new PageInfo(posts1);
                    return pageInfo;
                case "3":
                    PageHelper.startPage(pageNum,pageSize,order);
                    List<PostVO> posts2 = categoryMapper.selectListOrderByHot();
                    for (PostVO postVO :posts2){
                        postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                        Good good = goodMapper.selectByPostId(postVO.getPostId());
                        if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
                    }
                    PageInfo pageInfo2 = new PageInfo(posts2);
                    return pageInfo2;
                case "4":
                    PageHelper.startPage(pageNum,pageSize,order);
                    List<PostVO> posts3 = categoryMapper.selectEssencesList();
                    for (PostVO postVO :posts3){
                        postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
                        Good good = goodMapper.selectByPostId(postVO.getPostId());
                        if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                            postVO.setGoodNum(list.size());}
                    }
                    PageInfo pageInfo3 = new PageInfo(posts3);
                    return pageInfo3;
            }
        }
        PageInfo pageInfo = new PageInfo(posts);
        return pageInfo;
    }

    //新增分类
    @Override
    public ApiRestResponse addCategory(String categoryName, MultipartFile file, HttpServletRequest request) throws WebforumException {
        if (StringUtils.isEmpty(categoryName)) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Category category = categoryMapper.selectByName(categoryName);
        if (category !=null){
            if (category.getVisibility()==0){
                throw new WebforumException(WebforumExceptionEnum.CATEGORY_EXISTED);
            }else {
                category.setVisibility(0);
            }
        }
        String newFileName = UserServiceImpl.createFileName(file);
        //创建文件夹
        File fileDirectory = new File(Constant.CATEGORY_UPLOAD_DIR);
        //目标文件
        File destFile = new File(Constant.CATEGORY_UPLOAD_DIR + newFileName);
        UserServiceImpl.addFile(file, fileDirectory, destFile);
        String path = null;
        try {
            path = UserServiceImpl.getHost(new URI(request.getRequestURL() + "")) + "/categoryImages/" + newFileName;
        } catch (URISyntaxException e) {
            throw new WebforumException(WebforumExceptionEnum.ADDCATEGORY_FAILED);
        }
        if (category !=null){
            category.setCategoryImage(path);
            int i = categoryMapper.updateByPrimaryKeySelective(category);
            if (i!=1){
                throw new WebforumException(WebforumExceptionEnum.ADD_FAILED);
            }
            return ApiRestResponse.success();
        }
        Category newcategory = new Category();
        newcategory.setCategoryName(categoryName);
        newcategory.setCategoryImage(path);
        int count = categoryMapper.insertSelective(newcategory);
        if (count!=1){
            throw new WebforumException(WebforumExceptionEnum.ADD_FAILED);
        }
        return ApiRestResponse.success();
    }

    //获取分类
    @Override
    public ApiRestResponse getCategory() {
        List<CategoryVO> categoryVOS = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();
        for (Category category:categories){
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            categoryVO.setPostNum(categoryMapper.selectPostNum(category.getCategoryId()));
            categoryVOS.add(categoryVO);
        }
        return ApiRestResponse.success(categoryVOS);
    }

    @Override
    public PageInfo getListForRollBackCategory(Integer pageNum, Integer pageSize) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        PageHelper.startPage(pageNum,pageSize,"category_id,update_time desc");
        List<PostVO> posts = categoryMapper.selectRollBackList();
        PageInfo pageInfo = new PageInfo(posts);
        return pageInfo;
    }

    //模糊查询帖子
    @Override
    public PageInfo SearchList(Integer pageNum, Integer pageSize, String content) throws WebforumException {
        if (pageNum==null||pageSize==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(content)){
            return new PageInfo();
        }
        PageHelper.startPage(pageNum,pageSize,"update_time desc");
        List<PostVO> posts = categoryMapper.searchListByContent(content);
        for (PostVO postVO:posts){
            Good good = goodMapper.selectByPostId(postVO.getPostId());
            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                postVO.setGoodNum(list.size());}
        }
        PageInfo pageInfo = new PageInfo(posts);
        return pageInfo;
    }

    //修改分类名称
    @Override
    public void updateCategoryNameById(Long categoryId, String categoryName) throws WebforumException {
        if (StringUtils.isEmpty(categoryId)||StringUtils.isEmpty(categoryName)){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        category.setCategoryName(categoryName);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count!=1){
            throw new WebforumException(WebforumExceptionEnum.UPATE_CATEGORYNAME_FAILED);
        }
    }

    //修改分类图片
    @Override
    public void updateCategoryImageById(HttpServletRequest request,Long categoryId, MultipartFile categoryImage) throws WebforumException {
        if (StringUtils.isEmpty(categoryId)||categoryImage==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        //生成文件名UUID+文件名
        String newFileName = UserServiceImpl.createFileName(categoryImage);
        //创建文件夹
        File fileDirectory = new File(Constant.CATEGORY_UPLOAD_DIR);
        //目标文件
        File destFile = new File(Constant.CATEGORY_UPLOAD_DIR + newFileName);
        UserServiceImpl.addFile(categoryImage, fileDirectory,destFile);
        String oldCategoryImage = category.getCategoryImage();
        File file = new File(Constant.CATEGORY_UPLOAD_DIR+oldCategoryImage.substring(oldCategoryImage.lastIndexOf("/")));
        file.delete();
        try {
            String path = UserServiceImpl.getHost(new URI(request.getRequestURL() + "")) + "/categoryImages/" + newFileName;
            category.setCategoryImage(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count!=1){
            throw new WebforumException(WebforumExceptionEnum.UPATE_CATEGORYIMAGE_FAILED);
        }
    }

    //修改分类推荐状态
    @Override
    public void updateCategoryRecommend(Long categoryId, int type) throws WebforumException {
        if (categoryId==null||StringUtils.isEmpty(type)) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (type==0){
            category.setIsRecommend((byte) 0);
        }else if (type==1){
            category.setIsRecommend((byte) 1);
        }else {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count!=1){
            if (type==0){
                throw new WebforumException(WebforumExceptionEnum.UNRECOMMEND_FAILED);
            }
            throw new WebforumException(WebforumExceptionEnum.RECOMMEND_FAILED);
        }
    }

    //删除分类
    @Override
    public int deleteCategoryById(Long categoryId) throws WebforumException {
        if (categoryId==null){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        int deletePostCount = postMapper.deleteByCategoryId(categoryId);
        int count = categoryMapper.deleteCategory(categoryId);
        if (count!=1){
            throw new WebforumException(WebforumExceptionEnum.DELETECATEGORY_FAILED);
        }
        return deletePostCount;
    }

}
