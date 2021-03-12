package com.kcbs.webforum.service;

public interface SendMailService {
    public boolean sendMail(String toMail,String subject,String content);
}
