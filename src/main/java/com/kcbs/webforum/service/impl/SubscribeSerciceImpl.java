package com.kcbs.webforum.service.impl;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.SubscribeMapper;
import com.kcbs.webforum.model.dao.UserMapper;
import com.kcbs.webforum.model.pojo.Subscribe;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.service.SubscribeService;
import com.kcbs.webforum.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class SubscribeSerciceImpl implements SubscribeService {
    @Resource
    SubscribeMapper subscribeMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public ApiRestResponse subscribe(Long beSubscribe, HttpServletRequest request) throws WebforumException {
        User user = JwtUtils.getUser(request);
        if (StringUtils.isEmpty(beSubscribe)||beSubscribe==user.getUserId()){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (userMapper.selectByPrimaryKey(beSubscribe)==null){
            throw new WebforumException(WebforumExceptionEnum.SUBSCRIBE_FAILED);
        }
        Subscribe result = subscribeMapper.selectBybeSubscribeAndsubscribe(beSubscribe,user.getUserId());
        if (result!=null) {
            result.setIsDel((byte)0);
            result.setUpdateTime(null);
            subscribeMapper.updateByPrimaryKeySelective(result);
            return ApiRestResponse.success();
        }
        Subscribe newsub = new Subscribe();
        newsub.setIsDel((byte) 0);
        newsub.setBeSubscribe(beSubscribe);
        newsub.setSubscribe(user.getUserId());
        newsub.setUpdateTime(new Date(System.currentTimeMillis()));
        subscribeMapper.insertSelective(newsub);
        return ApiRestResponse.success();
    }

    @Override
    public ApiRestResponse cancelsubscribe(Long beSubscribe, HttpServletRequest request) throws WebforumException{
        User user = JwtUtils.getUser(request);
        if (StringUtils.isEmpty(beSubscribe)||beSubscribe==user.getUserId()){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (userMapper.selectByPrimaryKey(beSubscribe)==null){
            throw new WebforumException(WebforumExceptionEnum.DELSUBSCRIBE_FAILED);
        }
        Subscribe result = subscribeMapper.selectBybeSubscribeAndsubscribe(beSubscribe,user.getUserId());
        if (result!=null) {
            result.setIsDel((byte)1);
            result.setUpdateTime(null);
            subscribeMapper.updateByPrimaryKeySelective(result);
            return ApiRestResponse.success();
        }
        return ApiRestResponse.error(WebforumExceptionEnum.SYSTEM_ERROR);
    }

    @Override
    public ApiRestResponse checkSubscribeById(Long userId, HttpServletRequest request) throws WebforumException {
        if (StringUtils.isEmpty(userId)){
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        int i = subscribeMapper.checkSubscribeById(userId, JwtUtils.getUser(request).getUserId());
        if (i==1){
            return ApiRestResponse.success(true);
        }
        return ApiRestResponse.success(false);

    }
}
