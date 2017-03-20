package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.domain.mahua.JokeImgHua;
import com.oupeng.joke.spider.domain.mahua.JokeTextHua;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.PostConstruct;

/**
 * 快乐麻花
 * Created by zongchao on 2017/3/13.
 */
@Component
public class HuaSpiderTask {
    private static final Logger logger = LoggerFactory.getLogger(HuaSpiderTask.class);

    //快乐麻花
    private String textUrlHua;
    private String imgUrlHua;


    @Autowired
    private Environment env;

    private Site site = Site.me()
            .setRetryTimes(3)
            .setRetrySleepTime(10000)
            .setSleepTime(5000)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html）");

    @Qualifier("JobInfoDaoImgPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoImgPipeline;


    @Qualifier("JobInfoDaoPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoPipeline;


    @PostConstruct
    public void init() {


        String ht = env.getProperty("hua.spider.text.url");
        if (StringUtils.isNotBlank(ht)) {
            textUrlHua = ht;
        }
        String hi = env.getProperty("hua.spider.img.url");
        if (StringUtils.isNotBlank(hi)) {
            imgUrlHua = hi;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
//            spiderHua();
        }

    }

    /**
     * 抓取快乐麻花
     */
    @Scheduled(cron = "0 40 2 * * ?")
    public void spiderHua() {
        logger.info("kuailemahua spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgHua.class, imgUrlHua);
        logger.info("kuailemahua spider text...");
        crawl(jobInfoDaoPipeline, JokeTextHua.class, textUrlHua);

    }


    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
