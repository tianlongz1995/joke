package com.oupeng.joke.spider.domain.quishibaike;

import com.oupeng.joke.spider.domain.JokeImg;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

@TargetUrl("https://www.qiushibaike.com/article/\\d+")
@HelpUrl("https://www.qiushibaike.com/imgrank/|https://www.qiushibaike.com/imgrank/page/\\d+/?s=5001144")
public class JokeImgQuiShiImg extends JokeImg implements AfterExtractor {

    private static final int new_sourceId = 136;
    /**
     * 标题
     */
    @ExtractBy(value = "//div[@id='single-next-link']/div[@class='content']/allText()", notNull = true)
    private String title;
    /**
     * 图片地址
     */
    @ExtractBy(value = "//div[@id='single-next-link']/div[@class='thumb']/img/@src", notNull = true)
    private String img;
    /**
     * 来源
     */
    private String src;
    /**
     * 数据源
     */
    private Integer sourceId;
    /**
     * 评论数量
     */
    private Integer commentNumber;
    /**
     * 评论点赞
     */
    @ExtractBy("//article[@id=\"godCmt\"]//div[@class=\"comments-table\"]/a//div[@class=\"likenum\"]/allText()")
    private List<Integer> hotGoods;
    /**
     * 评论内容
     */
    @ExtractBy("//article[@id=\"godCmt\"]//div[@class=\"comments-table\"]/a//div[@class=\"main-text\"]/text()")
    private List<String> hotContents;


    @Override
    public Integer getSourceId() {
        return new_sourceId;
    }

    @Override
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String getSrc() {
        return src;
    }

    @Override
    public void setSrc(String src) {
        this.src = src;
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

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();

        //抓取到的神评数量
        if (!CollectionUtils.isEmpty(this.getHotGoods()) && !CollectionUtils.isEmpty(this.getHotContents())) {
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }
    }
}

