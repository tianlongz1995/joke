package com.oupeng.joke.spider.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by pengzheng on 17-6-19.
 */
public class DuowanProcessor implements PageProcessor {

    //打印日志
    private static final Logger logger = LoggerFactory.getLogger(DuowanProcessor.class);
    private static final String LIST_URL = "http://tu.duowan.com/m/bxgif.*";
    private static final String ARITICALE_URL = "http://tu.duowan.com/gallery/\\d+.html";
    //设置抓取网站的相关配置
    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36")
            .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .setCharset("UTF-8");

    @Override
    public void process(Page page) {

        //定义如何抽取页面信息
        //抽取标题

       // logger.info("Json抽取查看：" + page.getJson().jsonPath("$.name").get().toString());

        page.putField("title", page.getHtml().xpath("//div[@id='show']/div[@class='title']/h1/text()").toString());
        //抽取图片源址
        page.putField("src", page.getHtml().xpath("//div[@class='tips clearfix']/div[@class='fr']/a[@class='full']/@href").toString());

        logger.info("页面信息如下：" + page.getHtml().toString());
        logger.info(page.getRawText());
        //logger.info("Json抽取:" + new JsonPathSelector("$(data.url)").select(page.getRawText()));
        if (page.getResultItems().get("src") == null) {
            //如果没有抽取到源地址则跳过此页面
            page.setSkip(true);
        } //将源地址从此页面下载
        else {
            String src = page.getResultItems().get("src").toString();
            String title = page.getResultItems().get("title").toString();
            logger.info("title:" + title + " src:" + src);
        }
        //从当前页面中发现后续链接
        logger.info(page.getHtml().links().regex("(http://tu.duowan.com/gallery/\\d+.html(#p\\d+)*)").all().toString());
        page.addTargetRequests(page.getHtml().links().regex("(http://tu.duowan.com/gallery/\\d+.html(#p\\d+)*)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
