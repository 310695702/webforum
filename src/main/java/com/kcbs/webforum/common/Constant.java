package com.kcbs.webforum.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：     常量值
 */
@Configuration
public class Constant {

    public static final String WEBFORUM_USER = "web_forum_user";
    public static final String SALT = "da35s4bva2/213";
    public static final String APP_SECRET = "sadiau8AJKSkjaLJKHAS09"; //秘钥
    public static final Integer TIME = 7*60*60*24; //登录过期时间7天
    public static final String SUBJECT = "Webforum验证";
    public static final String CONTENT1 = "[Webforum验证]这是您的验证码:";
    public static final String CONTENT2 = "请不要将验证码透露给他人,有效期5分钟";
    public static String FILE_UPLOAD_DIR;
    public static String IMAGE_UPLOAD_DIR;
    public static String CATEGORY_UPLOAD_DIR;
    public static String DOWNLOAD_DIR;

    @Value("${file.download.dir}")
    public void setDownloadDir(String downloadDir) {
        DOWNLOAD_DIR = downloadDir;
    }

    @Value("${category.upload.dir}")
    public void setCategoryUploadDir(String categoryUploadDir){
        CATEGORY_UPLOAD_DIR = categoryUploadDir;
    }
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR = fileUploadDir;
    }
    @Value("${file.Image.dir}")
    public void setImageUploadDir(String imageUploadDir) {
        IMAGE_UPLOAD_DIR = imageUploadDir;
    }

    public static String HOST;
    @Value("${spring.redis.host}")
    public void setHost(String host){
        HOST = host;
    }
    public static Integer PORT;
    @Value("${spring.redis.port}")
    public void setPort(Integer port){
        PORT = port;
    }
}
