package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.wowant.JokeImgWoWant;
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
 * Created by yeluo on 2017/6/16.
 */
@Component
public class SpiderTask_WoWant {

    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_WoWant.class);

    private String imgUrl;

    @Autowired
    private Environment env;

    private Site site = Site.me()
            .setRetryTimes(3)
            .setRetrySleepTime(10000)
            .setSleepTime(5000)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html）");


    // @Qualifier("JobInfoDaoImgPipeline")
    @Qualifier("JobInfoDao_ImgPipeline")
    @Autowired
    private PageModelPipeline JobInfoDao_ImgPipeline;


    @PostConstruct
    public void init() {
        String wowant = env.getProperty("wowant.spider.img.url");
        if (StringUtils.isNotBlank(wowant)) {
            imgUrl = wowant;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spider();
        }
    }


    /**
     *  wowant 我想网
     *
     */
    @Scheduled(cron = "0 20 23 * * ?")
    public void spider() {
        logger.info("wowant spider image...");
        crawl(JobInfoDao_ImgPipeline, JokeImgWoWant.class, imgUrl);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {

        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();

    }

}

