package com.oupeng.joke.spider;

/**
 * Created by hushuang on 2017/3/8.
 */
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1500);

    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor()).addUrl("http://www.laifudao.com/wangwen/youmoxiaohua.htm").thread(1).run();
    }

    @Override
    public void process(Page page) {


        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.laifudao\\.com/\\w+/\\w+_\\d{1,2}\\.htm)").all());
//        列表页
        List<String> list = page.getHtml().links().regex("(http://www\\.laifudao\\.com/\\w+/\\w+_\\d{1,2}\\.htm)").all();
        page.addTargetRequests(list);

        List<String> details = page.getHtml().links().regex("(http://www\\.laifudao\\.com/\\w+/\\d{6,8}\\.htm)").all();

        page.addTargetRequests(details);

        if(page.getUrl().regex("(http://www\\.laifudao\\.com/\\w+/\\d{6,8}\\.htm)").match()){
            page.putField("title", page.getHtml().xpath("//header[@class='post-header']//a/text()").toString());
            page.putField("content", page.getHtml().xpath("//div[@class='post-content stickem-container']//p/text()").toString());
            System.out.println("爬取到详情页:" + page.getResultItems().get("title") );
        }

//        if (page.getResultItems().get("name")==null){
//            //skip this page
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }

    @Override
    public Site getSite() {
        return site;
    }
}