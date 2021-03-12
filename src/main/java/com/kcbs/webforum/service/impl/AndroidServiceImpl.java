package com.kcbs.webforum.service.impl;

import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.model.dao.AndroidMapper;
import com.kcbs.webforum.service.AndroidService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AndroidServiceImpl implements AndroidService {
    @Resource
    AndroidMapper androidMapper;


    @Override
    public ApiRestResponse selectVersion() {
        List<Double> list = new ArrayList<>();
        list.add(androidMapper.selectVersion());
        list.add(androidMapper.selectVersionName());
        return ApiRestResponse.success(list);
    }

}
