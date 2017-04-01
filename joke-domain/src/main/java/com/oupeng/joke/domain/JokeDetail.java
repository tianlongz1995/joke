package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

public class JokeDetail {
	@JsonInclude(Include.NON_NULL)
	private String title;
	@JsonInclude(Include.NON_NULL)
	private String content;
	@JsonInclude(Include.NON_NULL)
	private String img;
	@JsonInclude(Include.NON_NULL)
	private String gif;
    /** (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)     */
	@JsonInclude(Include.NON_NULL)
	private Integer type;
	@JsonInclude(Include.NON_NULL)
	private Integer good;
	@JsonInclude(Include.NON_NULL)
	private Integer bad;
	@JsonInclude(Include.NON_NULL)
	private Integer nextId;
	@JsonInclude(Include.NON_NULL)
	private Integer lastId;
	@JsonInclude(Include.NON_NULL)
	private List<Joke> relatedImg;
	/** 评论数量(commentNumber)	*/
	@JsonInclude(Include.NON_NULL)
	private Integer commentNumber;
    /** 来源网址	*/
    @JsonInclude(Include.NON_NULL)
    private String src;
    /** 发布用户头像  releaseAvatar */
    @JsonInclude(Include.NON_NULL)
    private String ra;
    /** 发布用户昵称 releaseNick */
    @JsonInclude(Include.NON_NULL)
    private String rn;


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
	public Integer getNextId() {
		return nextId;
	}
	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}
	public Integer getLastId() {
		return lastId;
	}
	public void setLastId(Integer lastId) {
		this.lastId = lastId;
	}
	public List<Joke> getRelatedImg() {
		return relatedImg;
	}
	public void setRelatedImg(List<Joke> relatedImg) {
		this.relatedImg = relatedImg;
	}

	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }
}
