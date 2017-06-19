package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.neihan.JokeImgNeiHanJS;
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
 * Created by xiongyingl on 2017/6/14.
 */
@Component
public class SpiderTask_NeiHan {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_NeiHan.class);

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
        String neihan = env.getProperty("neihan.spider.img.url");
        if (StringUtils.isNotBlank(neihan)) {
            imgUrl = neihan;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spider();
        }
    }


    /**
     *  neihan
     *
     */
    @Scheduled(cron = "0 20 1 * * ?")
    public void spider() {
        logger.info("neihan spider image...");
        crawl(JobInfoDao_ImgPipeline, JokeImgNeiHanJS.class, imgUrl);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {

        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();

    }

}
