package com.kcbs.webforum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.model.request.UserInfoReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    void register(String username,String password,String veriftcode) throws WebforumException;

    void getCode(String username,String email) throws WebforumException;

    ApiRestResponse login(String username, String password, HttpServletRequest request) throws WebforumException;

    void updatepersonalizedSignature(String personalizedSignature, HttpServletRequest request) throws WebforumException, JsonProcessingException;

    ApiRestResponse adminlogin(String username, String password,HttpServletRequest request) throws WebforumException;

    ApiRestResponse uploadhead(HttpServletRequest request, MultipartFile file) throws WebforumException;

    ApiRestResponse getFuns(HttpServletRequest request, Long beSubscribe, Integer pageNum, Integer pageSize)throws WebforumException;

    Boolean checkAdminRole(User currentUser);

    ApiRestResponse getCategoryByUser(HttpServletRequest request) throws WebforumException;

    ApiRestResponse SubscribeCategory(HttpServletRequest request, Long categoryId) throws WebforumException;

    ApiRestResponse delSubscribeCategory(HttpServletRequest request, Long categoryId) throws WebforumException;

    ApiRestResponse getUser(HttpServletRequest request,Long userId) throws WebforumException;

    ApiRestResponse getSubscribeList(HttpServletRequest request, Long subscribe, Integer pageNum, Integer pageSize) throws WebforumException;

    ApiRestResponse getUserFunsNum(HttpServletRequest request, Long userId) throws WebforumException;

    ApiRestResponse getUserSubscribeNum(HttpServletRequest request, Long userId) throws WebforumException;

    ApiRestResponse findPassword(String email) throws WebforumException;

    ApiRestResponse updatePassword(String password,HttpServletRequest request) throws WebforumException, NoSuchAlgorithmException;

    ApiRestResponse banUserForId(Long userId, Integer banTime, String banMsg) throws WebforumException;

    ApiRestResponse unBanUserForId(Long userId) throws WebforumException;

    ApiRestResponse getUserByAdmin(Long userId) throws WebforumException;

    ApiRestResponse banUserForName(String username, Integer banTime, String banMsg) throws WebforumException;

    ApiRestResponse unBanUserForName(String username) throws WebforumException;

    ApiRestResponse searchUser(Integer pageNum, Integer pageSize, String content) throws WebforumException;

    void improveInfo(UserInfoReq userInfo, HttpServletRequest request) throws WebforumException;
}
