package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.PostImages;

import java.util.List;

public interface PostImagesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PostImages record);

    int insertSelective(PostImages record);

    PostImages selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PostImages record);

    int updateByPrimaryKey(PostImages record);

    List<String> selectPostImages(long id);
}