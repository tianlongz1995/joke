package com.oupeng.joke.back.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Comment;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

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
				setSocketTimeout(2000 * 30).//1分钟
				setConnectTimeout(2000 * 30).//1分钟
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

    /**
     * 获取随机用户信息
     * @param randomUserUrl
     * @return
     */
    public static Comment getRandomUser(String randomUserUrl) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(randomUserUrl);
        Comment comment = new Comment();
            try {
            httpClient = HttpClients.createDefault();
                response = httpClient.execute(httpGet);
                String result = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject().parseObject(result.toString().trim());
                if(jsonObject != null && jsonObject.get("avata") != null && jsonObject.get("nick") != null){
                    comment.setAvata(jsonObject.get("avata").toString());
                    comment.setNick(jsonObject.get("nick").toString());
                }
            return comment;
        } catch (IOException e) {
            logger.error("upload img to crop error !",e);
            return comment;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("upload img to crop error !", e);
            }
        }
    }

    /**
     * 获取远程URL图片名称
     * @param img
     * @return
     */
    public static String getUrlImageFileName(String img) {
        int pos = img.lastIndexOf("/");
        if(pos < 0){
            return null;
        }
        return img.substring(pos + 1);
    }

	/**
	 * 发起http get请求获取网页源代码
	 *
	 * @param requestUrl String  请求地址
	 * @return String  该地址返回的html字符串
	 */
	public static String httpRequest(String requestUrl) {

		StringBuffer buffer = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		HttpURLConnection httpUrlConn = null;

		try {
			// 建立get请求
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.setDoInput(true);

			// 获取输入流
			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			// 从输入流读取结果
			buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

		} catch (Exception e) {
			logger.error("重爬joke,连接["+requestUrl+"]异常"+e.getMessage(),e);
			return null;
		} finally {
			// 释放资源
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
			} catch (IOException e) {
				logger.error("重爬joke,关闭[" + requestUrl + "]连接异常" + e.getMessage(), e);
			}
		}
		return buffer.toString();
	}

}
