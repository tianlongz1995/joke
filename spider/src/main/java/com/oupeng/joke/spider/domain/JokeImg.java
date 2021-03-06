package com.oupeng.joke.spider.domain;


import java.util.List;

/**
 * change by xiongyingl on 2017/6/15. 增加了评论相关List,可添加多条神评论
 */
public class JokeImg {

    private Integer id;

    private String title;

    private String img;

    private String gif;

    private Integer width;

    private Integer height;

    /**
     * 类型(0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)
     */
    private Integer type;
    /**
     *  点赞数
     */
    private Integer good;
    /**
     *  踩数
     */
    private Integer bad;
    /**
     *  状态 0:未审核 1:通过（审核） 2:不通过 3:已发布 4:已推荐 5:下线 6:已置顶
     */
    private Integer status;
    /**
     * 段子来源html地址
     */
    private String src;
    /**
     * 数据源编号
     */
    private Integer sourceId;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 废弃字段
     */
    private Integer agreeTotal;
    /**
     * 昵称
     */
    private String nick;
    /**
     * 评论
     */
    private Comment comment;
    /**
     * 用户头像URL
     */
    private String avata;
    /**
     * 神评数量
     */
    private Integer commentNumber;
    /**
     *  神评点赞数(与评论内容一一对应)
     */
    private List<Integer> hotGoods;
    /**
     * 神评内容
     */
    private List<String> hotContents;
    /**
     * 发布人头像
     */
    private String releaseAvata;
    /**
     * 发布人昵称
     */
    private String releaseNick;


    public List<Integer> getHotGoods() {
        return hotGoods;
    }

    public void setHotGoods(List<Integer> hotGoods) {
        this.hotGoods = hotGoods;
    }

    public List<String> getHotContents() {
        return hotContents;
    }

    public void setHotContents(List<String> hotContents) {
        this.hotContents = hotContents;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    public Integer getAgreeTotal() {
        return agreeTotal;
    }

    public void setAgreeTotal(Integer agreeTotal) {
        this.agreeTotal = agreeTotal;
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

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getReleaseAvata() {
        return releaseAvata;
    }

    public void setReleaseAvata(String releaseAvata) {
        this.releaseAvata = releaseAvata;
    }

    public String getReleaseNick() {
        return releaseNick;
    }

    public void setReleaseNick(String releaseNick) {
        this.releaseNick = releaseNick;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }
}
