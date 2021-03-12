package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Post;
import com.kcbs.webforum.model.vo.PostVO;

import java.util.List;

public interface PostMapper {
    int deleteByPrimaryKey(Long postId);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Long postId);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    Post selectByTitle(String name);

    Integer selectPostNumById(Long id);

    List<PostVO> selectByUserId(Long id);

    List<Post> selectListByPrimaryKey(List<Long> list);

    List<PostVO> selectSubscribePostList(Long userId);

    PostVO selectById(Long postId);

    long selectLastId();

    int deleteByCategoryId(Long categoryId);
}