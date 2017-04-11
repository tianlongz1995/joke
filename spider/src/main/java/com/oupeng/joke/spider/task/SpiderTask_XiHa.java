package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.xiha.JokeImgXiha;
import com.oupeng.joke.spider.domain.xiha.JokeTextXiha;
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
 * 嘻嘻哈哈
 * Created by zongchao on 2017/3/21.
 */
@Component
public class SpiderTask_XiHa {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_XiHa.class);

    //嘻嘻哈哈
    private String textUrlXiha;
    private String imgUrlXiha;


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
        String xhtext = env.getProperty("xiha.spider.text.url");
        if (StringUtils.isNotBlank(xhtext)) {
            textUrlXiha = xhtext;
        }
        String xhimg = env.getProperty("xiha.spider.img.url");
        if (StringUtils.isNotBlank(xhimg)) {
            imgUrlXiha = xhimg;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderXiha();
        }
    }


    /**
     * 抓取嘻嘻哈哈
     */
    @Scheduled(cron = "0 30 23 * * ?")
    public void spiderXiha() {
        logger.info("xixihaha spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgXiha.class, imgUrlXiha);
        logger.info("xixihaha spider text...");
        crawl(jobInfoDaoPipeline, JokeTextXiha.class, textUrlXiha);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }
}
