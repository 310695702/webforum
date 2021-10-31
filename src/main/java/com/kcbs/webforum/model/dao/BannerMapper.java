package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.model.pojo.Banner;

import java.util.List;

public interface BannerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Banner record);

    int insertSelective(Banner record);

    Banner selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Banner record);

    int updateByPrimaryKey(Banner record);

    Banner selectByPostId(Long postId);

    List<Banner> selectAll();
}