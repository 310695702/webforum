package com.kcbs.webforum.service;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;

import javax.servlet.http.HttpServletRequest;

public interface SubscribeService {
    ApiRestResponse subscribe(Long beSubscribe, HttpServletRequest request) throws WebforumException;

    ApiRestResponse cancelsubscribe(Long beSubscribe, HttpServletRequest request) throws WebforumException;

    ApiRestResponse checkSubscribeById(Long userId, HttpServletRequest request) throws WebforumException;
}
