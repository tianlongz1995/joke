package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.domain.JokeText;
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
 * Created by zongchao on 2017/3/13.
 */
@Component
public class SpiderTask {
    private static Logger logger = LoggerFactory.getLogger(SpiderTask.class);

    private String textUrl;
    private String imgUrl;

    @Autowired
    private Environment env;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");

    @Qualifier("JobInfoDaoImgPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoImgPipeline;


    @Qualifier("JobInfoDaoPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoPipeline;


    @PostConstruct
    public void init() {
        String t = env.getProperty("spider.text.url");
        if (StringUtils.isNotBlank(t)) {
            textUrl = t;
        }
        String i = env.getProperty("spider.img.url");
        if (StringUtils.isNotBlank(i)) {
            imgUrl = i;
        }
    }


    @Scheduled(cron = "0 30 23 * * ?")
    public void spiderImg() {
        logger.info("spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImg.class, imgUrl);
        logger.info("spider text...");
        crawl(jobInfoDaoPipeline, JokeText.class, textUrl);

    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }
}
