package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.laifudao.JokeImgLai;
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
public class LaiSpiderTask {
    private static final Logger logger = LoggerFactory.getLogger(LaiSpiderTask.class);

    //来福岛
    private String textUrlLai;
    private String imgUrlLai;


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
        String lt = env.getProperty("lai.spider.text.url");
        if (StringUtils.isNotBlank(lt)) {
            textUrlLai = lt;
        }
        String li = env.getProperty("lai.spider.img.url");
        if (StringUtils.isNotBlank(li)) {
            imgUrlLai = li;
        }
        String isRun=env.getProperty("init.spider.run");
        if(isRun!=null&&isRun.equalsIgnoreCase("true")){
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

    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .start();
    }

}
