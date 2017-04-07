package com.oupeng.joke.spider.domain.niubi;



import com.oupeng.joke.spider.domain.JokeText;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://nbsw.cc/\\d{5,7}")
@HelpUrl("http://nbsw.cc/hot/page/\\d{1,2}")
public class JokeTextNiu extends JokeText implements AfterExtractor {


    @ExtractBy(value = "//div[@class='postcontent']/p/allText()", notNull = true)
    private String content;
    /**
     * 来源
     */
    private String src;
    /**
     * 内容源id
     */
    private Integer sourceId;
    /**
     * 评论内容
     */
    @ExtractBy("//div[@class='commentcontent']/p/allText()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//div[@class='commentcontent']//div[@class='counter']/span/text()")
    private String liketotal;

    private Integer agreeTotal;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getLiketotal() {
        return liketotal;
    }

    public void setLiketotal(String liketotal) {
        this.liketotal = liketotal;
    }

    public Integer getAgreeTotal() {

        return agreeTotal;
    }

    public void setAgreeTotal(Integer agreeTotal) {
        this.agreeTotal = agreeTotal;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return 145;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
        //点赞书设定
        if (StringUtils.isNotBlank(liketotal)) {
            String total = liketotal.replace("+", "");
            agreeTotal = Integer.valueOf(total);
        }
    }
}

