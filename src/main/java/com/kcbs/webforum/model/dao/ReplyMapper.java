package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Reply;
import com.kcbs.webforum.model.vo.ReplyVO;

import java.util.List;

public interface ReplyMapper {
    int deleteByPrimaryKey(Long replyId);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Long replyId);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    List<ReplyVO> selectByCommentId(Long commentId);

    int getNumByCommentId(Long commentId);

    List<ReplyVO> selectReplyByUser(Long userId);
}