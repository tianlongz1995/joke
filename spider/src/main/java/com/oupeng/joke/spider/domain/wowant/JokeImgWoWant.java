package com.oupeng.joke.spider.domain.wowant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.utils.HttpUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongyingl on 2017/6/16
 */

@HelpUrl("http://www.wowant.com/xieegif/index.html|http://www.wowant.com/xieegif/p_\\d{1,2}.html")
@TargetUrl("http://www.wowant.com/xieegif/\\d+.html")
public class JokeImgWoWant extends JokeImg implements AfterExtractor {


    private static final int new_sourceId = 156;

    @ExtractBy(value = "//div[@class=\"wowant_content\"]/h1/text()")
    private String title;

    @ExtractBy(value = "//div[@class=\"wowant_content\"]/div[@class=\"Article\"]/p[3]/img/@src", notNull = true)
    private String img;

    /**
     * 来源
     */
    private String src;

    /**
     * 新建数据源，需要重写sourceId：new_sourceId
     */
    private Integer sourceId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return new_sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {

        String pageURL = page.getUrl().toString();
        this.setSrc(pageURL);
    }
}


