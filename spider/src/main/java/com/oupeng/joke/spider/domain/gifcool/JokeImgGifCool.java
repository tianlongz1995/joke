package com.oupeng.joke.spider.domain.gifcool;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by xiongyingl on 2017/6/5.
 */

@TargetUrl("http://www.gifcool.com/\\w+/\\d+.html")
@HelpUrl("http://www.gifcool.com/gif/list_12_\\d{1,3}.html")
public class JokeImgGifCool extends JokeImg implements AfterExtractor {

    private static final String gifcoolUrl =  "http://www.gifcool.com";
    /**
     * 标题
     */
    @ExtractBy(value="//div[@class=\"colb\"]//div[@class=\"title\"]/strong/text()")
    private String title;
    /**
     * 图片
     */
    @ExtractBy(value="//div[@class=\"colb\"]//div[@class=\"con\"]//img/@src",notNull = true)
    private String img;
    /**
     * 来源
     */
    private String src;
    /**
     * 新建数据源时，需要重写（例如：gifcool->153）
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
        return 153;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {

        this.setSrc(page.getUrl().toString());
        /**
         * 类似于img=/uploads/allimg/170603/1-1F6031012214C.jpg
         */
        if (this.img != null && this.img.charAt(0) == '/') {
            this.setImg(gifcoolUrl + this.img);
        }
    }
}

