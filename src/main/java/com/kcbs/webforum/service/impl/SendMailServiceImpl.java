package com.kcbs.webforum.service.impl;

import com.kcbs.webforum.service.SendMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

@Service
public class SendMailServiceImpl implements SendMailService {
    @Resource
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private  String from;
    @Override
    public boolean sendMail(String toMail,String subject,String content){
        try {
            MimeMessage messageM = this.mailSender.createMimeMessage();
            MimeMessageHelper message=new MimeMessageHelper(messageM);
            message.setTo(toMail);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom(from);
            mailSender.send(messageM);
        }catch (Exception ex){
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return  Boolean.TRUE;
    }

}
