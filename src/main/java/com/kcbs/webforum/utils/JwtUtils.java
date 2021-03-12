package com.kcbs.webforum.utils;

import com.alibaba.fastjson.JSONObject;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.pojo.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {


    //生成token字符串的方法
    public static String getJwtToken(String id, String nickname){
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")	//头信息
                .setSubject("webforum-user")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("id", id)  //设置token主体部分 ，存储用户信息
                .claim("nickname", nickname)
                .signWith(SignatureAlgorithm.HS256, Constant.APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    public static ApiRestResponse checkToken(HttpServletRequest request) throws WebforumException{
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)){
            throw new WebforumException(WebforumExceptionEnum.NEED_LOGIN);
        }
        Jedis jedis =null;
        try {
            jedis=new Jedis(Constant.HOST,Constant.PORT);
            if (!jedis.exists(token)){
                throw new WebforumException(WebforumExceptionEnum.LOGIN_EXPIRED);
            }
            ApiRestResponse parse = JSONObject.parseObject(jedis.get(token),ApiRestResponse.class);
            return parse;
        }catch (IllegalArgumentException e){
            throw new WebforumException(WebforumExceptionEnum.LOGIN_EXPIRED);
        }catch (JedisConnectionException e){
            throw new WebforumException(WebforumExceptionEnum.JEDIS_FAILED);
        }
        finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    public static User getUser(HttpServletRequest request) throws WebforumException {
        ApiRestResponse res = checkToken(request);
        User user = JSONObject.parseObject(res.getData().toString(), User.class);
        return user;
    }

}
