package com.kcbs.webforum;

import com.kcbs.webforum.service.SendMailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;

@SpringBootTest
class WebforumApplicationTests {

    @Resource
    SendMailService service;
    @Test
    void contextLoads() {

    }

    @Test
    public void test(){


    }


}
