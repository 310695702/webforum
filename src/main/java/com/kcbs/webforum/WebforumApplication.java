package com.kcbs.webforum;

import com.kcbs.webforum.tcp.TcpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages = "com.kcbs.webforum.model.dao")
@EnableSwagger2
@EnableCaching
@EnableScheduling
public class WebforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebforumApplication.class, args);
        new TcpServer().start();
    }

}
