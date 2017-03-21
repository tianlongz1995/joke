package com.oupeng.joke.spider.domain.xiha;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.xxhh.com/content/\\d{6,9}.html")
@HelpUrl("http://www.xxhh.com/pics/page/\\d{1,2}")
public class JokeImgXiha extends JokeImg {

    private Integer id;

    @ExtractBy("//div[@class='article']/pre/text()")
    private String title;

    @ExtractBy("//div[@class='article']/img/@src")
    private String img;

    private String gif;

    @ExtractBy("//div[@class='article']/img/@width")
    private Integer width;

    @ExtractBy("//div[@class='article']/img/@height")
    private Integer height;
    /**
     * (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)
     */

    private Integer type;

    private Integer good;

    private Integer bad;

    private Integer status;


    /**
     * 来源
     */
    @ExtractBy("//div[@class='section']/@id")
    private String src;
    /**
     * 评论内容
     */


    private String commentContent;

    /**
     * 用户头像URL
     */
    @ExtractBy("//div[@class='comment-list-reply']/p/text()")
    private String avata;
    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//div[@class='comment-list-action']/a/span/text()")
    private Integer agreeTotal;
    /**
     * 昵称
     */

    private String nick;
    /**
     * 评论
     */

    private Comment comment;

    private Integer sourceId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getAgreeTotal() {
        return agreeTotal;
    }

    public void setAgreeTotal(Integer agreeTotal) {
        this.agreeTotal = agreeTotal;
    }

    public String getSrc() {
        String srcStr = "http://www.xxhh.com/content/" + StringUtils.substringAfter(src, "-") + ".html";
        return srcStr;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return 147;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

}

