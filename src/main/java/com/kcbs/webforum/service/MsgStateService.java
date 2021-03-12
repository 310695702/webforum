package com.kcbs.webforum.service;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;

import javax.servlet.http.HttpServletRequest;

public interface MsgStateService {
    ApiRestResponse checkState(HttpServletRequest request) throws WebforumException;

    ApiRestResponse list(HttpServletRequest request, Integer pageNum, Integer pageSize) throws WebforumException;
}
