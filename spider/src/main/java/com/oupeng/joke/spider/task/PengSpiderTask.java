package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.domain.pengfu.JokeImgPeng;
import com.oupeng.joke.spider.domain.pengfu.JokeTextPeng;
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
 * 捧腹网
 * Created by zongchao on 2017/3/20.
 */
//@Component
public class PengSpiderTask {
    private static final Logger logger = LoggerFactory.getLogger(PengSpiderTask.class);

    //捧腹网
    private String textUrlPeng;
    private String imgUrlPeng;


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
        String pt = env.getProperty("peng.spider.text.url");
        if (StringUtils.isNotBlank(pt)) {
            textUrlPeng = pt;
        }
        String pi = env.getProperty("peng.spider.img.url");
        if (StringUtils.isNotBlank(pi)) {
            imgUrlPeng = pi;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
             spiderPeng();
        }

    }


    /**
     * 抓取捧腹网
     */
     @Scheduled(cron = "0 30 5 * * ?")
    public void spiderPeng() {
        logger.info("pengfu spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgPeng.class, imgUrlPeng);
        logger.info("pengfu spider text...");
        crawl(jobInfoDaoPipeline, JokeTextPeng.class, textUrlPeng);

    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
