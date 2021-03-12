package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Comment;
import com.kcbs.webforum.model.vo.CommentVo;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Long commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    int getCommentNumById(Long id);

    Integer selectByPostId(Long postId);

    List<CommentVo> selectByParentId(Long postId);

    List<CommentVo> selectByUserId(Long userId);
}