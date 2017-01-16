package com.oupeng.joke.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.back.service.UploadService;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping(value="/upload")
public class UploadController {
	
	@Autowired
	private UploadService uploadService;
	
	@RequestMapping("/img")
	@ResponseBody
	public Result uploadLandingPageImage(HttpServletRequest request){
		MultipartFile image = ((MultipartHttpServletRequest)request).getFile("img");
		String result = uploadService.copyImg(image);
		if(StringUtils.isBlank(result)){
			return new Failed("图片上传失败");
		}
		return new Success(result);
	}

	//富文本框图片上传
	@RequestMapping("/richText")
	@ResponseBody
	public Result uploadRichTextImg(HttpServletRequest request){
		MultipartFile image = ((MultipartHttpServletRequest)request).getFile("wangEditorH5File");
		String result = uploadService.copyImg(image);
		if(StringUtils.isBlank(result)){
			return new Failed("图片上传失败");
		}
		return new Success(result);
	}

	/**
	 * choice banner 图片上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/cbImg")
	@ResponseBody
	public Result uploadChoiceImage(HttpServletRequest request){
		MultipartFile image = ((MultipartHttpServletRequest)request).getFile("img");
		BufferedImage imageBuffer;
		try {
			imageBuffer = ImageIO.read(image.getInputStream());
		} catch (IOException e) {
			return  new Failed("获取图片高度和宽度失败");
		}
		int width = imageBuffer.getWidth();
		int height = imageBuffer.getHeight();
		String result = uploadService.copyImg(image);
		if(StringUtils.isBlank(result)){
			return new Failed("图片上传失败");
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("url",result);
		jsonObject.put("width",width);
		jsonObject.put("height",height);
	    String jString = jsonObject.toString();
		return new Success(jString);
	}
}
