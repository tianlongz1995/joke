
package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.domain.xiee.JokeImgSexi;
import com.oupeng.joke.spider.domain.xiee.JokeImgXiee;
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
 * 邪恶漫画
 * Created by zongchao on 2017/3/23.
 */
//@Component
public class SpiderTask_Xiee {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_Xiee.class);

    //邪恶漫画
    private String imgUrlSexi;
    private String imgUrlXiee;


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
        String sexiImg = env.getProperty("xiee.spider.sexi.url");
        if (StringUtils.isNotBlank(sexiImg)) {
            imgUrlSexi = sexiImg;
        }
        String xieeImg = env.getProperty("xiee.spider.xiee.url");
        if (StringUtils.isNotBlank(xieeImg)) {
            imgUrlXiee = xieeImg;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderXiee();
        }
    }


    /**
     * 抓取邪恶漫画
     */
    @Scheduled(cron = "0 30 21 * * ?")
    public void spiderXiee() {
        logger.info("xieemanhua sexi spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgSexi.class, imgUrlSexi);
        logger.info("xieemanhua xiee spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgXiee.class, imgUrlXiee);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }
}
