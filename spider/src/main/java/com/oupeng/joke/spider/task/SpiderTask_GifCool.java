package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.gifcool.JokeImgGifCool;
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
 * 姐夫酷
 * Created by xiongyingl on 2017/6/5.
 */
@Component
public class SpiderTask_GifCool {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_GifCool.class);

    private String imgUrl;

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
        String gifcool = env.getProperty("gifcool.spider.img.url");
        if (StringUtils.isNotBlank(gifcool)) {
            imgUrl = gifcool;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderGifCool();
        }
    }

    /**
     * GifCool
     *
     * @Scheduled(cron = "* 5 10 * * ?") 不能这样设置，爬虫频率不宜过快，
     * 1）过快相当于多个线程爬同一资源,会导致爬取资源重复
     * 2）不停地请求带攻击性，会被屏蔽IP
     */
    @Scheduled(cron = "0 18 2 * * ?")
    public void spiderGifCool() {
        logger.info("gifcool spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgGifCool.class, imgUrl);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {

        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();

    }

}
