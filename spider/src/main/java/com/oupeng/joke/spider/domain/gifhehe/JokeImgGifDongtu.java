package com.oupeng.joke.spider.domain.gifhehe;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 *
 */
@TargetUrl("http://xx.yzz.cn/dongtu/\\d{5,8}/\\d{6,10}.shtml")
//@TargetUrl("http://www.mzitu.com/page/\\d{1,5}")
@HelpUrl("http://xx.yzz.cn/dongtu/\\d{3,6},\\d{1,3}.shtml")
public class JokeImgGifDongtu extends JokeImg implements AfterExtractor {

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
        //System.out.println("title： "+title);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getImg() {
        //System.out.println("地址： "+img);
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSrc() {
        //System.out.println("地址： "+src);
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
        System.out.println("title： "+title+" src:"+src+" img:"+img);
        src = page.getUrl().toString();
    }
}

