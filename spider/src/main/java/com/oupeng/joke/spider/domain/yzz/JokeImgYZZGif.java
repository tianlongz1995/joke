package com.oupeng.joke.spider.domain.yzz;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by pengzheng on 2017/6/13.
 *
 */
@TargetUrl("http://xx.yzz.cn/gif/\\d{3,5}-\\d{3,10}.shtml")
@HelpUrl("http://xx.yzz.cn/gif/\\d{4},\\d{2,5}.shtml")
public class JokeImgYZZGif extends JokeImg implements AfterExtractor {

    @ExtractBy(value = "//div[@id='article']/h1/allText()", notNull = true)
    private String title;

    @ExtractBy(value = "//div[@id='article']//img/@src", notNull = true)
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

