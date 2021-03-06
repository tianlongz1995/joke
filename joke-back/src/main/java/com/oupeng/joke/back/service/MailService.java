package com.oupeng.joke.back.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * 邮件服务
 * Created by hushuang on 16/7/15.
 */
@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private static final String MAIL_FROM = "monitor@oupeng.com";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 发送邮件
     * @param recipient 收件人
     * @param subject   主题（邮件标题）
     * @param content   邮件内容
     */
    public void sendMail(final String recipient, final String subject, final String content){
        taskExecutor.execute(new Runnable() {
            public void run() {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(MAIL_FROM);
                    message.setTo(recipient.split(";"));
                    message.setSubject(subject);
                    message.setText(content);
                    javaMailSender.send(message);
                } catch (Exception e) {
                    logger.error("邮件发送失败：[recipient:{},subject:{},subject:{}]， ERROR:{}",recipient,subject,content, e.getMessage());
                }
            }
        });
    }
    
    /**
     * 发送邮件
     * @param recipient 收件人
     * @param cc 抄送,分号分隔
     * @param subject   主题（邮件标题）
     * @param content   邮件内容
     */
    public void sendMail(final String recipient,final String cc, final String subject, final String content){
        taskExecutor.execute(new Runnable() {
            public void run() {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(MAIL_FROM);
                    message.setTo(recipient);
                    message.setCc(cc.split(";"));
                    message.setSubject(subject);
                    message.setText(content);
                    javaMailSender.send(message);
                } catch (Exception e) {
                    logger.error("邮件发送失败：[recipient:[{}], subject:[{}], subject:[{}], ERROR:{}" ,recipient,subject,content, e.getMessage());
                }
            }
        });
    }


}
