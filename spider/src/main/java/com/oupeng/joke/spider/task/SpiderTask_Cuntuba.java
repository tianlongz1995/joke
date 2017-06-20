
package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.domain.cuntuba.JokeImgCtb;
import com.oupeng.joke.spider.domain.cuntuba.JokeTextCtb;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.PostConstruct;

/**
 * 寸土吧
 * Created by zongchao on 2017/3/23.
 */
//@Component
public class SpiderTask_Cuntuba {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_Cuntuba.class);

    //寸土吧
    private String textUrlCtb;
    private String imgUrlCtb;


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
        String ctbText = env.getProperty("ctb.spider.text.url");
        if (StringUtils.isNotBlank(ctbText)) {
            textUrlCtb = ctbText;
        }
        String ctbImg = env.getProperty("ctb.spider.img.url");
        if (StringUtils.isNotBlank(ctbImg)) {
            imgUrlCtb = ctbImg;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderCtb();
        }
    }


    /**
     * 抓取寸土吧
     */
    @Scheduled(cron = "0 30 22 * * ?")
    public void spiderCtb() {
        logger.info("cuntuba spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgCtb.class, imgUrlCtb);
        logger.info("cuntuba spider text...");
        crawl(jobInfoDaoPipeline, JokeTextCtb.class, textUrlCtb);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }
}
