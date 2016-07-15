package com.oupeng.joke.back.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.TaskExecutor;
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
@ImportResource("classpath:mail.xml")
public class MailServer {
    private static final Logger logger = LoggerFactory.getLogger(MailServer.class);
    private static final String MAIL_FROM = "monitor@oupeng.com";
    @Value("${email.encoding}")
    private String defaultEncoding;
    @Value("${email.host}")
    private String emailHost;
    @Value("${email_port}")
    private Integer port;
    @Value("${email.username}")
    private String userName;
    @Value("${email.password}")
    private String password;
    @Value("${email.from}")
    private String from;
    @Value("${email.smtp.auth}")
    private String mailAuth;
    @Value("${email.smtp.timeout}")
    private String timeout;
    @Value("${mail.thead.minSize}")
    private Integer minSize;
    @Value("${mail.thead.maxSize}")
    private Integer maxSize;
    @Value("${mail.thead.queueSize}")
    private Integer queueSize;


    private JavaMailSenderImpl javaMailSender;
    /**
     * 注入Spring封装的异步执行器
     */
    private ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    private void emailSenderInit(){
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding(defaultEncoding);
        javaMailSender.setHost(emailHost);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);
        Properties pp = new Properties();
        pp.setProperty("mail.smtp.auth", mailAuth);
        pp.setProperty("mail.smtp.timeout", timeout);
        javaMailSender.setJavaMailProperties(pp);

        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(minSize);
        taskExecutor.setMaxPoolSize(maxSize);
        taskExecutor.setQueueCapacity(queueSize);
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
