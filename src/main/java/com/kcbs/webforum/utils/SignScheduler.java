package com.kcbs.webforum.utils;

import com.kcbs.webforum.model.dao.UCSubscribeMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional(rollbackFor = Exception.class)
public class SignScheduler {

    @Resource
    UCSubscribeMapper ucSubscribeMapper;

    @Scheduled(cron = "0 0 0 * * ?",zone = "GMT+8")
    public void testTask(){
        ucSubscribeMapper.updateSignDays();
        ucSubscribeMapper.updateSign();
    }
}
