package com.kcbs.webforum.service.impl;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.dao.BannerMapper;
import com.kcbs.webforum.model.dao.PostMapper;
import com.kcbs.webforum.model.pojo.Banner;
import com.kcbs.webforum.model.pojo.Post;
import com.kcbs.webforum.service.BannerService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Transactional(rollbackFor = Exception.class)
public class BannerServiceImpl implements BannerService {
    @Resource
    BannerMapper bannerMapper;
    @Resource
    PostMapper postMapper;

    @Override
    public void addBanner(HttpServletRequest request, Long postId, MultipartFile file) throws WebforumException {
        if (postId == null || file == null) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Banner banner = bannerMapper.selectByPostId(postId);
        if (banner != null) {
            throw new WebforumException(WebforumExceptionEnum.CHECKBANNER_FAILED);
        }
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post == null) {
            throw new WebforumException(WebforumExceptionEnum.POST_EXISTED);
        }
        banner = new Banner();
        banner.setPostId(postId);
        String newfile = UserServiceImpl.createFileName(file);
        File fileDirectory = new File(Constant.BANNER_DIR);
        File destFile = new File(Constant.BANNER_DIR + newfile);
        UserServiceImpl.addFile(file, fileDirectory, destFile);
        try {
            String path = UserServiceImpl.getHost(new URI(request.getRequestURL() + "")) + "/bannerImages/" + newfile;
            banner.setRecommendImage(path);
            int count = bannerMapper.insertSelective(banner);
            if (count == 0) {
                throw new WebforumException(WebforumExceptionEnum.ADDBANNER_FAILED);
            }
        } catch (URISyntaxException e) {
            throw new WebforumException(WebforumExceptionEnum.ADDBANNER_FAILED);
        }

    }

    @Override
    @Cacheable(value = "getBanner")
    public ApiRestResponse getBanner() {
        return ApiRestResponse.success(bannerMapper.selectAll());
    }

    @Override
    public void deleteBanner(Long bannerId) throws WebforumException {
        if (bannerId == null) {
            throw new WebforumException(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Banner banner = bannerMapper.selectByPrimaryKey(bannerId);
        if (banner == null) {
            throw new WebforumException(WebforumExceptionEnum.BANNER_EXISTED);
        }
        String filename = banner.getRecommendImage().substring(banner.getRecommendImage().lastIndexOf("/"));
        File file = new File(Constant.BANNER_DIR + filename);
        file.delete();
        int count = bannerMapper.deleteByPrimaryKey(bannerId);
        if (count == 0) {
            throw new WebforumException(WebforumExceptionEnum.DELETEBANNER_FAILED);
        }
    }
}
