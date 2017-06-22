package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.laifudao.JokeImgLai;
import com.oupeng.joke.spider.domain.laifudao.JokeShenLai;
import com.oupeng.joke.spider.domain.laifudao.JokeTextLai;
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
 * 来福岛
 * Created by zongchao on 2017/3/13.
 */
@Component
public class SpiderTask_Lai {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask_Lai.class);

    //来福岛
    private String textUrlLai;
    private String imgUrlLai;
    private String shenUrlLai;


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
        String lt = env.getProperty("lai.spider.text.url");
        if (StringUtils.isNotBlank(lt)) {
            textUrlLai = lt;
        }
        String li = env.getProperty("lai.spider.img.url");
        if (StringUtils.isNotBlank(li)) {
            imgUrlLai = li;
        }
        String shen = env.getProperty("lai.spider.shen.url");
        if (StringUtils.isNotBlank(shen)) {
            shenUrlLai = shen;
        }
        String isRun = env.getProperty("init.spider.run");
        if (isRun != null && isRun.equalsIgnoreCase("true")) {
            spiderLai();
        }
    }


    /**
     * 抓取来福岛
     */
    @Scheduled(cron = "0 10 1 * * ?")
    public void spiderLai() {
        logger.info("laifudao spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgLai.class, imgUrlLai);
        logger.info("laifudao spider text...");
        crawl(jobInfoDaoPipeline, JokeTextLai.class, textUrlLai);
        logger.info("laifudao spider shenhuifu...");
        crawl(jobInfoDaoPipeline, JokeShenLai.class, shenUrlLai);

    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
