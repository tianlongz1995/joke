package com.oupeng.joke.back.util;

public class ImgReqDto {
	private String img;
	private boolean isCrop;
	
	public ImgReqDto(){
		super();
	}
	
	public ImgReqDto(String img,boolean isCrop){
		this.img = img;
		this.isCrop = isCrop;
	}
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public boolean isCrop() {
		return isCrop;
	}
	public void setCrop(boolean isCrop) {
		this.isCrop = isCrop;
	}
}