package com.oupeng.joke.spider.domain.cuntuba;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.cuntuba520.com/gaoxiao/\\d{5,7}.html")
@HelpUrl("http://www.cuntuba520.com/gaoxiao/list_\\d{1,2}.html")
public class JokeImgCtb extends JokeImg implements AfterExtractor {

    @ExtractBy("//div[@class='main']//h3/allText()")
    private String title;

    @ExtractBy(value = "//div[@class='cont']//img/@src", notNull = true)
    private String img;
    /**
     * 来源
     */
    private String src;
    /**
     * 内容源
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
        return 149;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

