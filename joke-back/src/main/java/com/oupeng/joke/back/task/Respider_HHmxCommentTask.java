package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 重新爬取joke(遨游哈哈)神评
 */
@Component
public class Respider_HHmxCommentTask {
    private static final Logger logger = LoggerFactory.getLogger(Respider_HHmxCommentTask.class);
    @Autowired
    private JokeService jokeService;
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        String isRun = env.getProperty("hhmx.respider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            RespiderTask();
        }
    }

    /**
     * 2017-07-12 晚7:00开始
     */
    @Scheduled(cron="0 0 19 12 7 ?")
    public void RespiderTask() {
        logger.info("开始重新爬取joke(遨游哈哈)神评...");
        jokeService.addJokeComment("2017-07-12 19:00:00", 148, "hhmx");
        logger.info("重新爬取joke(遨游哈哈)神评结束");
    }
}
