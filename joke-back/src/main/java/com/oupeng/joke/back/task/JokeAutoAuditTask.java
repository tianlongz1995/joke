package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class JokeAutoAuditTask {
    private static Logger logger = LoggerFactory.getLogger(JokeAutoAuditTask.class);
    @Autowired
    private JokeService jokeService;

    @Autowired
    private Environment env;
    /** 发布策略    */
    private String auditStrategy = "0:200,1:200,2:100";
    /** 运行状态    */
    private boolean running = false;

//    @PostConstruct
    public void init() {
        String status = env.getProperty("joke.auto.audit.status");
        if (StringUtils.isNumeric(status) && status.equals("1")) {
            running = true;
            String auto = env.getProperty("joke.auto.audit");
            if (StringUtils.isNotBlank(auto)) {
                auditStrategy = auto;
            }
            logger.info("自动发布段子启动...");
        } else {
            logger.info("自动发布段子不需要启动!");
        }
    }

    /**
     * 自动发布段子
     *
     */
//    @Scheduled(cron = "0 0 17 * * ?")
    public void run() {
        try {
            if (running) {
                logger.info("自动发布段子开始...");
                long start = System.currentTimeMillis();
                String[] auditStrategyArray = auditStrategy.split(",");
                for (String item : auditStrategyArray) {
                    String[] items = item.split(":");
                    jokeService.autoAuditJoke(Integer.valueOf(items[0]), Integer.valueOf(items[1]));
                }
                long end = System.currentTimeMillis();
                logger.info("自动发布段子开始结束: 发布策略:{} 耗时:{}", auditStrategy, FormatUtil.getTimeStr(end - start));
            }
        } catch (Exception e) {
            logger.error("自动发布段子异常:" + e.getMessage(), e);
        }

    }
}
