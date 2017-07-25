package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.processor.NeihanImgProcessor;
import com.oupeng.joke.spider.processor.NeihanTextProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;

/**
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


    @Qualifier("ImgProcessorPipeline")
    @Autowired
    private Pipeline ImgProcessorPipeline;

    @Qualifier("TextProcessorPipeline")
    @Autowired
    private Pipeline TextProcessorPipeline;


    @PostConstruct
    public void init() {
        logger.info("内涵段子定时任务启动...");
        String neihanText = env.getProperty("neihan.spider.text.url");
        if (StringUtils.isNotBlank(neihanText)) {
            textUrl = neihanText;
        }
        String neihanPic = env.getProperty("neihan.spider.img.url");
        if (StringUtils.isNotBlank(neihanPic)) {
            imgUrl = neihanPic;
        }
        String isRun = env.getProperty("neihan.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spider();
        } else {
            logger.info("neihan.spider.run:{}", isRun);
        }
    }

    /**
     * neihan
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void spider() {
        logger.info("neihan spider text...");
        crawl(new NeihanTextProcessor(), textUrl, TextProcessorPipeline);

        logger.info("neihan spider image...");
        crawl(new NeihanImgProcessor(), imgUrl, ImgProcessorPipeline);
    }

    private void crawl(PageProcessor pageProcessor, String url, Pipeline pipeline) {
        Spider.create(pageProcessor)
                .addUrl(url)
                .addPipeline(pipeline)
                .thread(1)
                .run();
    }

}
