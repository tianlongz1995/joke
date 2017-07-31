package com.oupeng.joke.spider.domain.fh21;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("https://sex.fh21.com.cn/qsyk/\\w+/\\d+.html")
@HelpUrl("https://sex.fh21.com.cn/sex.fh21.com.cn/qsyk/\\w+/list_\\d+.html")
public class JokeImgFh21 extends JokeImg implements AfterExtractor {

    /**
     * 标题
     */
    @ExtractBy(value="//h1[@class=\"detaila\"]/text()")
    private String title;
    /**
     * 图片
     */
    @ExtractBy(value="//div[@id=\"eFramePic\"]/p/img/@src",notNull = true)
    private String img;
    /**
     * 来源
     */
    private String src;
    /**
     * 数据源
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
        this.setSrc(page.getUrl().toString());
    }
}

