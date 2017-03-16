package com.oupeng.joke.spider.task;

import com.oupeng.joke.spider.domain.laifudao.JokeImgLai;
import com.oupeng.joke.spider.domain.laifudao.JokeTextLai;
import com.oupeng.joke.spider.domain.mahua.JokeImgHua;
import com.oupeng.joke.spider.domain.mahua.JokeTextHua;
import com.oupeng.joke.spider.domain.niubi.JokeTextNiu;
import com.oupeng.joke.spider.domain.zuiyou.JokeImgZui;
import com.oupeng.joke.spider.domain.zuiyou.JokeTextZui;
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
 * Created by zongchao on 2017/3/13.
 */
@Component
public class SpiderTask {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTask.class);

    //来福岛
    private String textUrlLai;
    private String imgUrlLai;
    //最右
    private String textUrlZui;
    private String imgUrlZui;
    //快乐麻花
    private String textUrlHua;
    private String imgUrlHua;
    //牛逼思维
    private String textUrlNiu;


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

        String zt = env.getProperty("zui.spider.text.url");
        if (StringUtils.isNotBlank(zt)) {
            textUrlZui = zt;
        }
        String zi = env.getProperty("zui.spider.img.url");
        if (StringUtils.isNotBlank(zi)) {
            imgUrlZui = zi;
        }

        String ht = env.getProperty("hua.spider.text.url");
        if (StringUtils.isNotBlank(ht)) {
            textUrlHua = ht;
        }
        String hi = env.getProperty("hua.spider.img.url");
        if (StringUtils.isNotBlank(hi)) {
            imgUrlHua = hi;
        }

        String nt = env.getProperty("niu.spider.text.url");
        if (StringUtils.isNotBlank(nt)) {
            textUrlNiu = nt;
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

    /**
     * 抓取最右
     */
    //@Scheduled(cron = "0 10 2 * * ?")
    public void spiderZui() {
        logger.info("zuiyou spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgZui.class, imgUrlZui);
        logger.info("zuiyou spider text...");
        crawl(jobInfoDaoPipeline, JokeTextZui.class, textUrlLai);

    }

    /**
     * 抓取快乐麻花
     */
    @Scheduled(cron = "0 10 3 * * ?")
    public void spiderHua() {
        logger.info("kuailemahua spider image...");
        crawl(jobInfoDaoImgPipeline, JokeImgHua.class, imgUrlHua);
        logger.info("kuailemahua spider text...");
        crawl(jobInfoDaoPipeline, JokeTextHua.class, textUrlHua);

    }

    /**
     * 抓取牛逼思维
     */
    @Scheduled(cron = "0 40 4 * * ?")
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
