package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.yzz.JokeImgYZZDongTu;
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
public class SpiderTask_YZZDongTu {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_YZZDongTu.class);

    //获取图片资源的地址
    private String gifUrlDongtu;

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

        String hi = env.getProperty("gif.spider.dongtu.url");

        if (StringUtils.isNotBlank(hi)) {
            gifUrlDongtu = hi;
        }

        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderGif();
        }

    }

    /**
     * 抓取叶子猪网站邪恶动图
     */
    @Scheduled(cron = "0 10 3 * * ?")
    public void spiderGif() {
        logger.info("yzz spider gif...");
        crawl(jobInfoDaoImgPipeline, JokeImgYZZDongTu.class, gifUrlDongtu);
    }


    private void crawl(PageModelPipeline line, Class c, String url) {
        System.out.println("开启爬取任务");
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
