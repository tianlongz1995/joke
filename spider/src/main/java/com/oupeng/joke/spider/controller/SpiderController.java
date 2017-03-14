package com.oupeng.joke.spider.controller;

import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.domain.JokeText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * Created by Administrator on 2017/3/13.
 */
@Controller
public class SpiderController {

    private static final String textUrl = "http://www.laifudao.com/wangwen/";
    private static final String imgUrl = "http://www.laifudao.com/tupian/";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");

    @Qualifier("JobInfoDaoImgPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoImgPipeline;


    @Qualifier("JobInfoDaoPipeline")
    @Autowired
    private PageModelPipeline jobInfoDaoPipeline;


    @RequestMapping("/text")
    public String spiderText() {
        crawl(jobInfoDaoPipeline, JokeText.class, textUrl);
        return "text";
    }

    @RequestMapping("/image")
    public String spiderImg() {
        crawl(jobInfoDaoImgPipeline, JokeImg.class, imgUrl);
        return "image";
    }

    private void crawl(PageModelPipeline line, Class c, String url) {
        OOSpider.create(site, line, c)
                .addUrl(url)
                .thread(1)
                .run();
    }
}
