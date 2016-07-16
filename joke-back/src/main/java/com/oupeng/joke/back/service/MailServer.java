package com.oupeng.joke.back.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * 邮件服务
 * Created by hushuang on 16/7/15.
 */
@Service
public class MailServer {
    private static final Logger logger = LoggerFactory.getLogger(MailServer.class);
    private static final String MAIL_FROM = "monitor@oupeng.com";

    @Autowired
    private Environment env;

    private JavaMailSenderImpl javaMailSender;
    /**
     * 注入Spring封装的异步执行器
     */
    private ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    private void emailSenderInit(){
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding(env.getProperty("email.encoding"));
        javaMailSender.setHost(env.getProperty("email.host"));
        javaMailSender.setPort(Integer.valueOf(env.getProperty("email.port")));
        javaMailSender.setUsername(env.getProperty("email.username"));
        javaMailSender.setPassword(env.getProperty("email.password"));
        Properties pp = new Properties();
        pp.setProperty("mail.smtp.auth", env.getProperty("email.smtp.auth"));
        pp.setProperty("mail.smtp.timeout", env.getProperty("email.smtp.timeout"));
        javaMailSender.setJavaMailProperties(pp);
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(Integer.valueOf(env.getProperty("email.thead.minSize")));
        taskExecutor.setMaxPoolSize(Integer.valueOf(env.getProperty("email.thead.maxSize")));
        taskExecutor.setQueueCapacity(Integer.valueOf(env.getProperty("email.thead.queueSize")));
        taskExecutor.initialize();
    }

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
                    message.setTo(recipient);
                    message.setSubject(subject);
                    message.setText(content);
                    javaMailSender.send(message);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

}
