package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

public class Joke {
	@JsonInclude(Include.NON_NULL)
	private Integer id;
	@JsonInclude(Include.NON_NULL)
	private String title;
	@JsonInclude(Include.NON_NULL)
	private String content;
	@JsonInclude(Include.NON_NULL)
	private String img;
	@JsonInclude(Include.NON_NULL)
	private String gif;
	@JsonInclude(Include.NON_NULL)
	private Integer width;
	@JsonInclude(Include.NON_NULL)
	private Integer height;
    /** (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)     */
	@JsonInclude(Include.NON_NULL)
	private Integer type;
	@JsonInclude(Include.NON_NULL)
	private Integer good;
	@JsonInclude(Include.NON_NULL)
	private Integer bad;
	@JsonInclude(Include.NON_NULL)
	private Integer status;
	@JsonInclude(Include.NON_NULL)
	private Integer sourceId;
	@JsonInclude(Include.NON_NULL)
	private String sourceName;
	@JsonInclude(Include.NON_NULL)
	private String verifyUser;
	@JsonInclude(Include.NON_NULL)
	private Date verifyTime;
	@JsonInclude(Include.NON_NULL)
	private Date createTime;
	@JsonInclude(Include.NON_NULL)
	private Date updateTime;
	@JsonInclude(Include.NON_NULL)
	private String uuid;
	@JsonInclude(Include.NON_NULL)
	private Integer sort;
    /** 分享网址 */
	@JsonInclude(Include.NON_NULL)
	private String shareUrl;
    /** 来源网址	*/
    @JsonInclude(Include.NON_NULL)
    private String src;
	/** 评论数量(commentNumber)	*/
	@JsonInclude(Include.NON_NULL)
	private Integer commentNumber;
	/** 评论内容	*/
	@JsonInclude(Include.NON_NULL)
	private String commentContent;
	/** 用户头像URL	*/
	@JsonInclude(Include.NON_NULL)
	private String avata;
	/** 昵称	*/
	@JsonInclude(Include.NON_NULL)
	private String nick;
	/** 评论	*/
	@JsonInclude(Include.NON_NULL)
	private Comment comment;
    /**权重*/
	@JsonInclude(Include.NON_NULL)
	private Integer weight;
    /** 评审状态 */
    @JsonInclude(Include.NON_NULL)
    private Integer audit;


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

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getVerifyUser() {
		return verifyUser;
	}
	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

    public Integer getAudit() {
        return audit;
    }
    public void setAudit(Integer audit) {
        this.audit = audit;
    }
}
