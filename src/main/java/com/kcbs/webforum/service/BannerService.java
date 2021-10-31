package com.kcbs.webforum.service;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.exception.WebforumException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface BannerService {
    void addBanner(HttpServletRequest request, Long postId, MultipartFile file) throws WebforumException;

    ApiRestResponse getBanner();

    void deleteBanner(Long bannerId) throws WebforumException;
}
