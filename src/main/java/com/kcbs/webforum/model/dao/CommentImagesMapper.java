package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.CommentImages;

import java.util.List;

public interface CommentImagesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommentImages record);

    int insertSelective(CommentImages record);

    CommentImages selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentImages record);

    int updateByPrimaryKey(CommentImages record);

    String   selectByCommentId(Long commentId);
}