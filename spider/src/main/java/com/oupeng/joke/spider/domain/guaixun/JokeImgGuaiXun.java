package com.oupeng.joke.spider.domain.guaixun;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 *
 */
@TargetUrl("http://www.guaixun.com/html/content_\\d+.html")
@HelpUrl("http://www.guaixun.com/html/picture00_\\d+.html")
public class JokeImgGuaiXun extends JokeImg implements AfterExtractor {

    private static final int new_sourceId = 157;
    /**
     * 标题
     */
    @ExtractBy(value = "//div[@class='bt_b']/allText()", notNull = true)
    private String title;
    /**
     * 图片
     */
    @ExtractBy(value = "//div[@id=\"contentStyle\"]/a//img/@src", notNull = true)
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
        return new_sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

