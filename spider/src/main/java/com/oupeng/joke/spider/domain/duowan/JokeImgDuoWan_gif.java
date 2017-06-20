package com.oupeng.joke.spider.domain.duowan;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by Jane on 2017/6/13.
 */
@TargetUrl("http://tu.duowan.com/gallery/\\d+\\w+")
public class JokeImgDuoWan_gif extends JokeImg implements AfterExtractor {

    @ExtractBy(value = "//div[@id='show']/div[@class='title']/h1/text()", notNull = true)
    //@ExtractBy(value = "//div[@class='img-info-n']/div[@class='img-info-title g-textHidden']/text()")
    private String title;

    //@ExtractByUrl("http://(\\w|\\/|\\d|.)+.gif")
    @ExtractBy(value = "//div[@class='tips clearfix']/div[@class='fr']/a[@class='full']/@href", notNull = true)
    //@ExtractBy(value = "//div[@class='swiper-slide swiper-item ui-picShow__item swiper-slide-active']/img/@data-original")
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
        System.out.println("src:"+src +" title:"+ title+" img:"+img);
        src = page.getUrl().toString();

    }
}

