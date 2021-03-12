package com.kcbs.webforum.config;

import com.kcbs.webforum.filter.UserCheckNullFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCheckNullFilterConfig {

    @Bean
    public UserCheckNullFilter checkNullFilter(){
        return new UserCheckNullFilter();
    }

    @Bean(name = "checkFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(checkNullFilter());
        filterRegistrationBean.addUrlPatterns("/login");
        filterRegistrationBean.addUrlPatterns("/register");
        filterRegistrationBean.addUrlPatterns("/admin/login");
        filterRegistrationBean.setName("checkFilterConf");
        return filterRegistrationBean;
    }
}
