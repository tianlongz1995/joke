package com.oupeng.joke.spider.domain.hhmx;

import com.oupeng.joke.spider.domain.JokeText;
import com.oupeng.joke.spider.utils.HttpUtil;
import com.oupeng.joke.spider.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;
import java.util.Map;

/**
 */
@TargetUrl("http://www.haha.mx/joke/\\d{6,9}")
@HelpUrl("http://www.haha.mx/topic/13/new/\\d{1,2}")
public class JokeTextHahaMX extends JokeText implements AfterExtractor {

    /**
     * 内容
     */
    @ExtractBy(value = "//p[@class='word-wrap joke-main-content-text']/allText()", notNull = true)
    private String content;
    /**
     * 来源
     */
    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;
    /**
     * 内容源
     */
    private Integer sourceId;
    /**
     * 评论数量
     */
    private Integer commentNumber;
    /**
     * 评论点赞
     */
    private List<Integer> hotGoods;
    /**
     * 评论内容
     */
    private List<String> hotContents;
    /**
     * 图片地址
     */
    @ExtractBy("//img[@class='joke-main-content-img']/@src")
    private String img;

    @ExtractBy("//img[@class='joke-main-img lazy']/@src")
    private String textImg;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Integer getCommentNumber() {
        return commentNumber;
    }

    @Override
    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    @Override
    public List<Integer> getHotGoods() {
        return hotGoods;
    }

    @Override
    public void setHotGoods(List<Integer> hotGoods) {
        this.hotGoods = hotGoods;
    }

    @Override
    public List<String> getHotContents() {
        return hotContents;
    }

    @Override
    public void setHotContents(List<String> hotContents) {
        this.hotContents = hotContents;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return 148;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTextImg() {
        return textImg;
    }

    public void setTextImg(String textImg) {
        this.textImg = textImg;
    }

    @Override
    public void afterProcess(Page page) {
        this.setContent(content.trim());
        if (img != null || textImg != null || content.length() == 0) {
            page.setSkip(true);
        }

        String pageURL = page.getUrl().toString();
        this.setSrc(pageURL);

        //POST抓取神评
        Map<String, List> map = HttpUtil.getHHmxMsg(pageURL);
        if (!CollectionUtils.isEmpty(map) && !CollectionUtils.isEmpty(map.get("hotGoods")) && !CollectionUtils.isEmpty(map.get("hotContents"))) {
            this.setHotGoods(map.get("hotGoods"));
            this.setHotContents(map.get("hotContents"));
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }
    }
}

