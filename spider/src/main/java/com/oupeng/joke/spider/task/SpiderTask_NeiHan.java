package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.neihan.JokeImgNeiHanJS;
import com.oupeng.joke.spider.domain.neihan.JokeTextNeihan;
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
    private String textUrl;

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
    private PageModelPipeline JobInfoDaoImgPipeline;

    @Qualifier("JobInfoDaoPipeline")
    @Autowired
    private PageModelPipeline JobInfoDaoPipeline;


    @PostConstruct
    public void init() {
        String neihanText = env.getProperty("neihan.spider.text.url");
        if (StringUtils.isNotBlank(neihanText)) {
            textUrl = neihanText;
        }
        String neihanPic = env.getProperty("neihan.spider.img.url");
        if (StringUtils.isNotBlank(neihanPic)) {
            imgUrl = neihanPic;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spider();
        }
    }


    /**
     * neihan
     */
    @Scheduled(cron = "0 20 1 * * ?")
    public void spider() {
        logger.info("neihan spider text...");
        crawl(JobInfoDaoPipeline, JokeTextNeihan.class, textUrl);

        logger.info("neihan spider image...");
        crawl(JobInfoDaoImgPipeline, JokeImgNeiHanJS.class, imgUrl);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {

        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();

    }

}
