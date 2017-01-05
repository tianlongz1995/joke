package com.oupeng.joke.back.controller;

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

import javax.servlet.http.HttpServletRequest;

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
		return new Success(result, null);
	}

	@RequestMapping("/richText")
	@ResponseBody
	public Result uploadRichTextImg(HttpServletRequest request){
		MultipartFile image = ((MultipartHttpServletRequest)request).getFile("wangEditorH5File");
		String result = uploadService.copyImg(image);
		if(StringUtils.isBlank(result)){
			return new Failed("图片上传失败");
		}
		return new Success(result, null);
	}
}
