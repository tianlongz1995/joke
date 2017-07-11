package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.weird.JokeImgWeird;
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
 * 爬取 http://xx.yzz.cn/dongtu/
 * Created by pengzheng on 2017/6/13.
 */
@Component
public class SpiderTask_WeirdInfo {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_WeirdInfo.class);

    //获取图片资源的地址
    private String imgUrlWeird;

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

        String hi = env.getProperty("weird.spider.img.url");

        if (StringUtils.isNotBlank(hi)) {
            imgUrlWeird = hi;
        }

        String isRun = env.getProperty("yzz.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderGif();
        }

    }

    /**
     * 抓取怪迅网网站图片
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void spiderGif() {
        logger.info("yzz spider gif...");
        crawl(jobInfoDaoImgPipeline, JokeImgWeird.class, imgUrlWeird);
    }


    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
