package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.guaixun.JokeGifGuaiXun;
import com.oupeng.joke.spider.domain.guaixun.JokeImgGuaiXun;
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
 * 怪讯网
 */
@Component
public class SpiderTask_guaixun {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_guaixun.class);

    private String imgUrl;
    private String gifUrl;

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


    @PostConstruct
    public void init() {
        String url = env.getProperty("guaixun.spider.img.url");
        if (StringUtils.isNotBlank(url)) {
            imgUrl = url;
        }
        String igf = env.getProperty("guaixun.spider.gif.url");
        if (StringUtils.isNotBlank(igf)) {
            gifUrl = igf;
        }


        String isRun = env.getProperty("guaixun.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spider();
        }
    }

    /**
     * 怪迅网
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void spider() {
        logger.info("guaixun spider img...");
        crawl(jobInfoDaoImgPipeline, JokeImgGuaiXun.class, imgUrl);

        logger.info("guaixun spider img...");
        crawl(jobInfoDaoImgPipeline, JokeGifGuaiXun.class, gifUrl);
    }


    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
