package com.oupeng.joke.spider.domain.sina;

import com.oupeng.joke.spider.domain.JokeImg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by pengzheng on 2017/6/13.
 *
 */
@TargetUrl("http://gif.sina.cn/")
public class JokeImgSinaGif extends JokeImg implements AfterExtractor {
    private static final Logger logger = LoggerFactory.getLogger(JokeImgSinaGif.class);

    @ExtractBy(value = "//div[@id='gif_box opacity']/h2/a/allText()", notNull = true)
    private String title;

    @ExtractBy(value = "//div[@id='gif_box opacity']//img/@src", notNull = true)
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

        logger.info("title:"+title+" src:"+img);
        src = page.getUrl().toString();
    }
}

