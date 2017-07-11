package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.ManagerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 重新爬取joke(内涵段子)神评
 */
@Component
public class Respider_NeiHanCommentTask {
    private static final Logger logger = LoggerFactory.getLogger(Respider_NeiHanCommentTask.class);
    @Autowired
    private JokeService jokeService;
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        String isRun = env.getProperty("neihan.respider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            RespiderTask();
        }
    }

    /**
     * 2017-07-13 晚7:00开始
     */
    @Scheduled(cron="0 0 19 13 7 ?")
    public void RespiderTask() {
        logger.info("开始重新爬取joke(内涵段子)神评...");
        jokeService.addJokeComment("2017-07-01 00:00:00", 155, "neihan");
        logger.info("重新爬取joke(内涵段子)神评结束");
    }
}
