package com.oupeng.joke.spider.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by pengzheng on 17-6-19.
 */
public class AngularJSProcessor implements PageProcessor {
    private static final int voteNum = 1000;
    private static final Logger logger = LoggerFactory.getLogger(AngularJSProcessor.class);
    private static final String ARITICALE_URL = "http://tu.duowan.com/gallery/\\d+.html";
    private static final String LIST_URL = "http://tu.duowan.com/m/bxgif.*";
    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8");

    @Override
    public void process(Page page) {
        logger.info("解析所有目标页面的链接");
        List<String> relativeUrl = page.getHtml().xpath(ARITICALE_URL).all();
        logger.info("将目标链接的页面加入到目标请求队列中");
        page.addTargetRequests(relativeUrl);


//        if (page.getUrl().regex(LIST_URL).match()) {
//            List<String> ids = new JsonPathSelector("$.data[*]._id").selectList(page.getRawText());
//            if (CollectionUtils.isNotEmpty(ids)) {
//                for (String id : ids) {
//                    //根据URL链接构造请求
//                    page.addTargetRequest("http://tu.duowan.com/m/" + id);
//                }
//            }
//        } else {
//
//            page.putField("title", new JsonPathSelector("$.data.title").select(page.getRawText()));
//            page.putField("content", new JsonPathSelector("$.data.content").select(page.getRawText()));
//        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
