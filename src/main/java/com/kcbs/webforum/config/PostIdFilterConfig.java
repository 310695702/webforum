package com.kcbs.webforum.config;

import com.kcbs.webforum.filter.PostIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostIdFilterConfig {

    @Bean
    public PostIdFilter postIdFilter(){
        return new PostIdFilter();
    }

    @Bean(name = "PostIdFilterConf")
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(postIdFilter());
        filterRegistrationBean.addUrlPatterns("/deletepost");
        filterRegistrationBean.addUrlPatterns("/comment");
        filterRegistrationBean.addUrlPatterns("/admin/rollbackpost");
        filterRegistrationBean.setName("PostIdFilterConf");
        return filterRegistrationBean;
    }
}
