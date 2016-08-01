package com.oupeng.joke.back.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	public static ImgRespDto handleImg(String url,String imgUrl,boolean isCrop){
		String requestString = JSONObject.toJSONString(new ImgReqDto(imgUrl, isCrop));
		
		HttpPost httppost = new HttpPost(url);
		HttpEntity entity = EntityBuilder.create()
				.setText(requestString)
				.setContentType(ContentType.APPLICATION_JSON.withCharset("UTF-8"))
				.build();
		httppost.setEntity(entity);
		RequestConfig requestConfig = RequestConfig.custom().
				setSocketTimeout(2000).
				setConnectTimeout(2000).
				build();
		httppost.setConfig(requestConfig);
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		ImgRespDto imgRespDto = null;
		try {
			httpClient = HttpClients.createDefault();
			httpResponse = httpClient.execute(httppost);
			imgRespDto = JSON.parseObject(EntityUtils.toByteArray(httpResponse.getEntity()),ImgRespDto.class);
		} catch (IOException e) {
			logger.error("upload img to crop error !",e);
		} finally{
			if(httpResponse != null){
				try {
					httpResponse.close();
				} catch (IOException e) {
					logger.error("upload img to crop error !",e);
				}
			}
			
			if(httpClient != null){
				try {
					httpClient.close();
				} catch (IOException e) {
					logger.error("upload img to crop error !",e);
				}
			}
		}
		return imgRespDto;
	}
}
