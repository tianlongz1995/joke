package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.domain.niubi.JokeTextNiu;
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
 * 牛逼思维
 * Created by zongchao on 2017/3/13.
 */
@Component
public class NiuSpiderTask {
    private static final Logger logger = LoggerFactory.getLogger(NiuSpiderTask.class);

    //牛逼思维
    private String textUrlNiu;


    @Autowired
    private Environment env;

    private Site site = Site.me()
            .setRetryTimes(3)
            .setRetrySleepTime(10000)
            .setSleepTime(5000)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html）");



    @Qualifier("JobInfoDaoPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoPipeline;


    @PostConstruct
    public void init() {

        String nt = env.getProperty("niu.spider.text.url");
        if (StringUtils.isNotBlank(nt)) {
            textUrlNiu = nt;
        }
        String isRun=env.getProperty("init.spider.run");
        logger.info("爬虫初始化运行:{}",isRun);
        if(isRun!=null&&isRun.equalsIgnoreCase("true")){
//            spiderNiu();
        }


    }



    /**
     * 抓取牛逼思维
     */
    @Scheduled(cron = "0 10 3 * * ?")
    public void spiderNiu() {
        logger.info("niubisiwei spider text...");
        crawl(jobInfoDaoPipeline, JokeTextNiu.class, textUrlNiu);
    }


    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
