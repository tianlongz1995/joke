package com.oupeng.joke.spider.domain.hhmx;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 */
@TargetUrl("http://www.haha.mx/joke/\\d{6,9}")
@HelpUrl("http://www.haha.mx/topic/16/new/\\d{1,2}")
public class JokeImgHahaMX extends JokeImg implements AfterExtractor{

    /**
     * 标题
     */
    @ExtractBy(value = "//p[@class='word-wrap joke-main-content-text']/allText()", notNull = true)
    private String title;
    /**
     * 图片地址
     */
    @ExtractBy(value = "//img[@class='joke-main-content-img']/@src", notNull = true)
    private String img;
    /**
     * 来源
     */
    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;
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

    @Override
    public void afterProcess(Page page) {

    }

}

