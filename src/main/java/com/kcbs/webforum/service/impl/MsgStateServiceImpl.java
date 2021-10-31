package com.kcbs.webforum.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.model.dao.CategoryMapper;
import com.kcbs.webforum.model.dao.GoodMapper;
import com.kcbs.webforum.model.dao.MsgStateMapper;
import com.kcbs.webforum.model.pojo.Good;
import com.kcbs.webforum.model.pojo.MsgState;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.vo.PostVO;
import com.kcbs.webforum.service.MsgStateService;
import com.kcbs.webforum.utils.JwtUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MsgStateServiceImpl implements MsgStateService {
    @Resource
    MsgStateMapper msgStateMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    GoodMapper goodMapper;

    @Override
    public ApiRestResponse checkState(HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        PageHelper.startPage(1,1,"post_id");
        List<MsgState> states = msgStateMapper.selectByUser(user.getUserId());
        PageInfo pageInfo = new PageInfo(states);
        return ApiRestResponse.success(pageInfo.getTotal());
    }

    @Override
    public ApiRestResponse list(HttpServletRequest request, Integer pageNum, Integer pageSize) throws WebforumException {
        User user = JwtUtils.getUser(request);
        PageHelper.startPage(pageNum,pageSize,"post_id");
        List<PostVO> states = msgStateMapper.selectVoByUser(user.getUserId());
        for (PostVO postVO:states){
            postVO.setCommentNum(categoryMapper.selectCommentNum(postVO.getPostId()));
            Good good = goodMapper.selectByPostId(postVO.getPostId());
            if(good!=null){List<String> list = JSONObject.parseArray(good.getUsers(), String.class);
                postVO.setGoodNum(list.size());}
        }
        PageInfo pageInfo = new PageInfo(states);
        return ApiRestResponse.success(pageInfo);
    }
}
