package com.kcbs.webforum.utils;

import com.alibaba.fastjson.JSONObject;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.UserMapper;
import com.kcbs.webforum.model.pojo.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class JedisUtil {



    public static void updateUsertoRedis(User user,UserMapper userMapper) throws WebforumException {
        User newUser = userMapper.selectByPrimaryKey(user.getUserId());
        newUser.setPassword(null);
        Jedis jedis = null;
        try {
            jedis = new Jedis(Constant.HOST,Constant.PORT);
            //根据id获取内存中的token
            if (!jedis.exists(String.valueOf(newUser.getUserId()))){
                String token = JwtUtils.getJwtToken(String.valueOf(newUser.getUserId()),newUser.getUsername());
                String json = JSONObject.toJSONString(ApiRestResponse.success(newUser));
                jedis.set(token,json);
                jedis.set(String.valueOf(newUser.getUserId()),token);
                jedis.expire(token, Constant.TIME);
                jedis.expire(String.valueOf(newUser.getUserId()),Constant.TIME);
            }else {
                String json = JSONObject.toJSONString(ApiRestResponse.success(newUser));
                jedis.set(jedis.get(String.valueOf(newUser.getUserId())),json);
                jedis.set(String.valueOf(newUser.getUserId()),jedis.get(String.valueOf(newUser.getUserId())));
                jedis.expire(jedis.get(String.valueOf(newUser.getUserId())), Constant.TIME);
                jedis.expire(String.valueOf(newUser.getUserId()),Constant.TIME);
            }
        }catch (JedisConnectionException e){
            e.printStackTrace();
            throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
        }
        finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }
}
