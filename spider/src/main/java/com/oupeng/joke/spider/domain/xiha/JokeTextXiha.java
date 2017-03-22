package com.oupeng.joke.spider.domain.xiha;


import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeText;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.xxhh.com/content/\\d{6,9}.html")
@HelpUrl("http://www.xxhh.com/duanzi/page/\\d{1,2}")
public class JokeTextXiha extends JokeText {
    private Integer id;

    private String title;
    @ExtractBy(value = "//div[@class='article']/pre/text()", notNull = true)
    private String content;

    /**
     * (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)
     */

    private Integer type;

    /**
     * 来源
     */
    @ExtractBy("//div[@class='section']/@id")
    private String src;

    /**
     * 内容源
     */
    private Integer sourceId;

    /**
     * 评论内容
     */
    @ExtractBy("//div[@class='comment-list-reply']/p/text()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//div[@class='comment-list-action']/a/span/text()")
    private Integer agreeTotal;
    /**
     * 用户头像URL
     */

    private String avata;
    /**
     * 昵称
     */

    private String nick;


    /**
     * 评论
     */

    private Comment comment;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

