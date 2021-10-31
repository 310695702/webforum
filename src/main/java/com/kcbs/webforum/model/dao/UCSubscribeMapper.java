package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Category;
import com.kcbs.webforum.model.pojo.UCSubscribe;

import java.util.List;

public interface UCSubscribeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UCSubscribe record);

    int insertSelective(UCSubscribe record);

    UCSubscribe selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UCSubscribe record);

    int updateByPrimaryKey(UCSubscribe record);

    List<Category> getCategoryByUserId(Long userId);

    List<UCSubscribe> selectByUCID(Long userId,Long categoryId);

    int UserSign(Long userId, Long categoryId);

    int updateSign();

    int updateSignDays();

    int post(Long userId, Long categoryId);

    int comment(Long userId, Long categoryId);

    int selectExpByUserId(Long userId);
}