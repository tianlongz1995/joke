package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.JokeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 重新爬取joke(遨游哈哈)神评
 */
@Component
public class Respider_HHmxCommentTask {
    private static final Logger logger = LoggerFactory.getLogger(Respider_HHmxCommentTask.class);
    /**
     * 判断重爬线程是否在运行中
     */
    private static AtomicBoolean isRun = new AtomicBoolean(false);
    private static String respiderTime = "2017-07-25 00:00:00";
    @Autowired
    private JokeService jokeService;
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        String time = env.getProperty("hhmx.respider.time");
        if (time != null) {
            respiderTime = time;
        }
        //时间格式检验
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(respiderTime);
        }catch(Exception e){
            logger.error("时间格式验证异常[yyyy-MM-dd HH:mm:ss]:"+e.getMessage(),e);
            return;
        }
        String isRun = env.getProperty("hhmx.respider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            respider();
        }
    }

    /**
     * 重爬任务:每间隔1小时爬取500条
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void respider() {
        if (!isRun.get()) {
            isRun.set(true);
            new Thread(new RespiderThread()).start();
        } else {
            logger.info("重爬线程还在运行中...");
        }
    }

    class RespiderThread implements Runnable {
        @Override
        public void run() {
            try {
                logger.info("开始重爬joke(遨游哈哈)神评...");
                jokeService.addJokeComment(respiderTime, 148, "hhmx");
                logger.info("重爬joke(遨游哈哈)神评结束");
            } catch (Exception e) {
                logger.error("重爬joke(遨游哈哈)异常:" + e.getMessage(), e);
            } finally {
                isRun.set(false);
            }
        }
    }
}
