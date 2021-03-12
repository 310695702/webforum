package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.MsgState;
import com.kcbs.webforum.model.vo.PostVO;

import java.util.List;

public interface MsgStateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MsgState record);

    int insertSelective(MsgState record);

    MsgState selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MsgState record);

    int updateByPrimaryKey(MsgState record);

    List<MsgState> selectByUser(Long userId);

    List<PostVO> selectVoByUser(Long userId);

    MsgState selectByDID(Long userId, Long postId);
}