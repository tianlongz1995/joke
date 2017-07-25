package com.oupeng.joke.spider.processor;

import com.oupeng.joke.spider.domain.JokeText;
import com.oupeng.joke.spider.utils.HttpUtil;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Map;

/**
 */
public class NeihanTextProcessor implements PageProcessor {

    /**
     * 因页面url特殊性，需要先判断详情页,再载入列表页(if-else)
     */
    private static final String LIST_URL = "http://neihanshequ.com/*";
    private static final String URL = "http://neihanshequ.com/p6\\d+/";

    //设置抓取网站的相关配置
    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setCharset("UTF-8");

    @Override
    public void process(Page page) {

        //详情页
        if (page.getUrl().regex(URL).match()) {
            JokeText jokeText = new JokeText();
            jokeText.setSourceId(155);

            //爬文字段子
            String img = page.getHtml().xpath("//img[@id=\"groupImage\"]/@src").toString();
            if (img != null) {
                page.setSkip(true);
            }
            jokeText.setContent(page.getHtml().xpath("//h1[@class=\"title\"]/p/text()").toString());

            String src = page.getUrl().toString();
            jokeText.setSrc(src);
            //神评论
            String str = src.substring("http://neihanshequ.com/p".length(), src.length() - 1);
            String jsonURL = "http://neihanshequ.com/m/api/get_essay_comments/?group_id=" + str + "&app_name=neihanshequ_web&offset=0";
            Map<String, List> map = HttpUtil.getNeiHanGodMsg(jsonURL);
            if (CollectionUtils.isEmpty(map) || CollectionUtils.isEmpty(map.get("hotGoods")) || CollectionUtils.isEmpty(map.get("hotContents"))) {
                jokeText.setCommentNumber(0);
            } else {
                int commentNumber = map.get("hotGoods").size();
                jokeText.setCommentNumber(commentNumber);
                jokeText.setHotGoods(map.get("hotGoods"));
                jokeText.setHotContents(map.get("hotContents"));
            }
            page.putField("jokeText", jokeText);
        }
        //列表页
        else {
            page.addTargetRequest("http://neihanshequ.com/?t=" + System.currentTimeMillis());
            page.addTargetRequests(page.getHtml().links().regex(URL).all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) {
//        Spider.create(new NeihanTextProcessor()).addUrl("http://neihanshequ.com/").run();
//    }
}
