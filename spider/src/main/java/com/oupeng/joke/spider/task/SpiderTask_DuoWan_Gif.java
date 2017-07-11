package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.pipeline.JobInfoDaoImgPipeLine_DuoWan;
import com.oupeng.joke.spider.processor.AngularJSProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.PostConstruct;

/**
 * Created by pengzheng on 2017/6/13.
 */
@Component
public class SpiderTask_DuoWan_Gif {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_DuoWan_Gif.class);

    //获取图片资源的地址
    private String imgUrlDuoWan_Gif;

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

        String hi = env.getProperty("duowan.spider.gif.url");

        if (StringUtils.isNotBlank(hi)) {
            imgUrlDuoWan_Gif = hi;
        }

        String isRun = env.getProperty("duowan.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderGif();
        }

    }

    /**
     * 抓取怪迅网网站图片
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void spiderGif() {
        logger.info("guixun spider gif...");
        crawl(imgUrlDuoWan_Gif);
    }


    private void crawl(String url) {
        logger.info("开启JSON爬取模式");
        Spider.create(new AngularJSProcessor())
                .addPipeline(new JobInfoDaoImgPipeLine_DuoWan())
                .addUrl(imgUrlDuoWan_Gif)
                .thread(1)
                .start();
    }

}
