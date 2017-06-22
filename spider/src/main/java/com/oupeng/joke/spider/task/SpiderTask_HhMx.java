package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.hhmx.JokeImgHahaMX;
import com.oupeng.joke.spider.domain.hhmx.JokeTextHahaMX;
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
 * 哈哈mx
 * Created by zongchao on 2017/3/21.
 */
@Component
public class SpiderTask_HhMx {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_HhMx.class);

    //哈哈MX
    private String textUrlHhMx;
    private String imgUrlHhMx;


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
        String xhtext = env.getProperty("haha.spider.text.url");
        if (StringUtils.isNotBlank(xhtext)) {
            textUrlHhMx = xhtext;
        }
        String xhimg = env.getProperty("haha.spider.img.url");
        if (StringUtils.isNotBlank(xhimg)) {
            imgUrlHhMx = xhimg;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderHhMx();
        }

    }

    /**
     * 哈哈MX
     */
    @Scheduled(cron = "0 30 22 * * ?")
    public void spiderHhMx() {
        logger.info("hahaMX spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgHahaMX.class, imgUrlHhMx);
        logger.info("hahaMX spider text...");
        crawl(jobInfoDaoPipeline, JokeTextHahaMX.class, textUrlHhMx);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }
}
