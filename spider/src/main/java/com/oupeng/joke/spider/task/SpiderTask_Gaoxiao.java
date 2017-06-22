package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.gaoxiao.JokeHotGao;
import com.oupeng.joke.spider.domain.gaoxiao.JokeImgGao;
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
 * 搞笑gif
 * Created by java_zong on 2017/4/11.
 */
@Component
public class SpiderTask_Gaoxiao {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_Gaoxiao.class);
    //推荐
    private String imgUrlGao;
    //热门
    private String hotUrlGao;


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
        String img = env.getProperty("gaoxiao.spider.img.url");
        if (StringUtils.isNotBlank(img)) {
            imgUrlGao = img;
        }
        String hot = env.getProperty("gaoxiao.spider.hot.url");
        if (StringUtils.isNotBlank(hot)) {
            hotUrlGao = hot;
        }
        String isRun = env.getProperty("gaoxiao.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderGaoxiao();
        }
    }

    /**
     * 3GIFS
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void spiderGaoxiao() {
        logger.info("搞笑gif spider recommended image...");
        crawl(jobInfoDaoImgPipeline, JokeImgGao.class, imgUrlGao);
        logger.info("搞笑gif spider hot image...");
        crawl(jobInfoDaoImgPipeline, JokeHotGao.class, hotUrlGao);
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }
}
