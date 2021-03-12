package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.vo.UserVo;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(String name);

    User selectByEmail(String email);

    List<UserVo> getSubscribeId(Long userId);

    List<UserVo> getBeSubscribeId(Long userid);

    List<User> searchUserByString(String content);

    Integer selectWordNumberByUserId(Long userId);
}