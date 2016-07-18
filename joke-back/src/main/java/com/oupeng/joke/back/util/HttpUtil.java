package com.oupeng.joke.back.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
public class HttpUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	@SuppressWarnings("resource")
	public static ImgRespDto handleImg(String url,String imgUrl,boolean isCrop){
		String requestString = JSONObject.toJSONString(new ImgReqDto(imgUrl, isCrop));
		
		HttpPost httppost = new HttpPost(url);
		HttpEntity entity = EntityBuilder.create()
				.setText(requestString)
				.setContentType(ContentType.APPLICATION_JSON.withCharset("UTF-8"))
				.build();
		httppost.setEntity(entity);
		HttpResponse httpResponse = null;
		byte[] content;
		ImgRespDto imgRespDto = null;
		try {
			httpResponse = new DefaultHttpClient().execute(httppost);
			content = EntityUtils.toByteArray(httpResponse.getEntity());
			imgRespDto = JSON.parseObject(content,ImgRespDto.class);
		} catch (IOException e) {
			logger.error("upload img to crop error !",e);
		}
		return imgRespDto;
	}
}
