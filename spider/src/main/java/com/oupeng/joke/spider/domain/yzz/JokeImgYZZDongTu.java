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
@TargetUrl("http://xx.yzz.cn/dongtu/\\d{5,8}/\\d{6,10}.shtml|http://xx.yzz.cn/dongtu/\\d{5,8}/\\d{6,10}_\\d+.shtml")
@HelpUrl("http://xx.yzz.cn/dongtu/\\d{3,6},\\d{1,3}.shtml|http://xx.yzz.cn/dongtu/\\d{5,8}/\\d{6,10}_\\d+.shtml")
public class JokeImgYZZDongTu extends JokeImg implements AfterExtractor {

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
        return 154;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

