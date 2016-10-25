package com.oupeng.joke.back.util;

public class ImgRespDto {
	private String imgUrl;
	private String gifUrl;
	private Integer width;
	private Integer height;
	private Integer errorCode;
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getGifUrl() {
		return gifUrl;
	}
	public void setGifUrl(String gifUrl) {
		this.gifUrl = gifUrl;
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
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("ImgRespDto{");
		sb.append("imgUrl='").append(imgUrl).append('\'');
		sb.append(", gifUrl='").append(gifUrl).append('\'');
		sb.append(", width=").append(width);
		sb.append(", height=").append(height);
		sb.append(", errorCode=").append(errorCode);
		sb.append('}');
		return sb.toString();
	}
}