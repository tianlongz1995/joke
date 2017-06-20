package com.oupeng.joke.spider.domain.weird;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by Jane on 2017/6/13.
 *
 */
@TargetUrl("http://www.guaixun.com/html/content_\\d{5,7}.html")
@HelpUrl("http://www.guaixun.com/index_\\d+.html")
public class JokeImgWeird extends JokeImg implements AfterExtractor {

    @ExtractBy(value = "//div[@class='bt_b']/allText()", notNull = true)
    private String title;

    @ExtractBy(value = "//div[@class='nr']//img/@src", notNull = true)
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
        return 157;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

