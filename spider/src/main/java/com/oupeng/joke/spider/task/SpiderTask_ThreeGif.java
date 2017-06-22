package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.domain.threegif.JokeFunnyThreeGif;
import com.oupeng.joke.spider.domain.threegif.JokeImgThreeGif;
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
 * 3GIFS
 * Created by java_zong on 2017/4/11.
 */
//@Component
public class SpiderTask_ThreeGif {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_ThreeGif.class);

    private String imgUrlThree;
    private String funnyUrlThree = "http://www.3gifs.com/cate/搞笑";


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
        String threeGif = env.getProperty("3gif.spider.img.url");
        if (StringUtils.isNotBlank(threeGif)) {
            imgUrlThree = threeGif;
        }
        String isRun = env.getProperty("3gif.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderThree();
        }
    }

    /**
     * 3GIFS
     */
    @Scheduled(cron = "0 30 2 * * ?")
    public void spiderThree() {
        logger.info("3GIFS spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgThreeGif.class, imgUrlThree);
        logger.info("3GIFS spider funny image...");
        crawl(jobInfoDaoImgPipeline, JokeFunnyThreeGif.class, funnyUrlThree);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
