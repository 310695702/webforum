package com.kcbs.webforum.config;

import com.kcbs.webforum.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述：     配置地址映射
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/download/**")
                .addResourceLocations("file:" + Constant.DOWNLOAD_DIR);
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
        registry.addResourceHandler("/PostImages/**")
                .addResourceLocations("file:" + Constant.POSTIMAGE_UPLOAD_DIR)
        ;registry.addResourceHandler("/CommentImages/**")
                .addResourceLocations("file:" + Constant.COMMENTIMAGE_UPLOAD_DIR);
        registry.addResourceHandler("/categoryImages/**")
                .addResourceLocations("file:" + Constant.CATEGORY_UPLOAD_DIR);
        registry.addResourceHandler("/bannerImages/**")
                .addResourceLocations("file:" + Constant.BANNER_DIR);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist/");
       }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowCredentials(true)
//                .allowedHeaders("*");
//    }
}
