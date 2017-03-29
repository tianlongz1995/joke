package com.oupeng.joke.spider.domain.cuntuba;


import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeText;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.cuntuba520.com/duanzi/\\d{5,7}.html")
@HelpUrl("http://www.cuntuba520.com/duanzi/list_\\d{1,2}.html")
public class JokeTextCtb extends JokeText implements AfterExtractor {

    @ExtractBy("//div[@class='main']//h3/allText()")
    private String title;
    @ExtractBy(value = "//div[@class='cont']/allText()", notNull = true)
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrc() {
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
        src = page.getUrl().toString();
    }
}

