package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Subscribe;

public interface SubscribeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Subscribe record);

    int insertSelective(Subscribe record);

    Subscribe selectByPrimaryKey(Long id);

    Subscribe selectBybeSubscribeAndsubscribe(Long beSubscribe,Long subscribe);

    int updateByPrimaryKeySelective(Subscribe record);

    int updateByPrimaryKey(Subscribe record);

    int checkSubscribeById(Long be_subscribe, Long subscribe);
}