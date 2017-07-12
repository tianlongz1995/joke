package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.JokeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 重新爬取joke(来福岛)神评
 */
@Component
public class Respider_LaiCommentTask {
    private static final Logger logger = LoggerFactory.getLogger(Respider_LaiCommentTask.class);
    @Autowired
    private JokeService jokeService;
    @Autowired
    private Environment env;

    private String respiderTime = "2017-07-12 19:00:00";

    @PostConstruct
    public void init() {
        String time = env.getProperty("laifudao.respider.time");
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
        String isRun = env.getProperty("laifudao.respider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            new Thread(new RespiderThread()).start();
        }
    }

    class RespiderThread implements Runnable{
        @Override
        public void run() {
            try{
                logger.info("开始重爬joke(来福岛)神评...");
                jokeService.addJokeComment(respiderTime, 141, "laifudao");
                logger.info("重爬joke(来福岛)神评结束");
            }catch(Exception e){
                logger.error("重爬joke(来福岛)异常:" + e.getMessage(),e);
            }
        }
    }

}
