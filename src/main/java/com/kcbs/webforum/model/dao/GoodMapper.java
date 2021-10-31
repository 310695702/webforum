package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Good;

public interface GoodMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Good record);

    int insertSelective(Good record);

    Good selectByPrimaryKey(Long id);

    Good selectByPostId(Long id);

    int updateByPrimaryKeySelective(Good record);

    int updateByPrimaryKeyWithBLOBs(Good record);

    int updateByPrimaryKey(Good record);
}