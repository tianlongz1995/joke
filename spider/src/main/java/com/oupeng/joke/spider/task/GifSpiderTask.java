package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.gifhehe.JokeImgGifhehe;
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
 * Created by Jane on 2017/6/5.
 */
@Component
public class GifSpiderTask {
    private static final Logger logger = LoggerFactory.getLogger(GifSpiderTask.class);

    //获取图片资源的地址
    private String gifUrlGaoxiao;

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

        String hi = env.getProperty("gif.spider.gaoxiao.url");

        System.out.println("输出初始化爬去页面:"+hi);
        if (StringUtils.isNotBlank(hi)) {
            gifUrlGaoxiao = hi;
        }

        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderGif();
        }

    }

    /**
     * 抓取新浪网站
     */
    @Scheduled(cron = "0 40 2 * * ?")
    public void spiderGif() {
        logger.info("gifhehe spider gif...");
        crawl(jobInfoDaoImgPipeline, JokeImgGifhehe.class, gifUrlGaoxiao);
    }


    private void crawl(PageModelPipeline line, Class c, String url) {
        System.out.println("开启爬取任务");
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
