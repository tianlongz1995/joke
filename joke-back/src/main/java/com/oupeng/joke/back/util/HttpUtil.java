package com.oupeng.joke.back.util;

import java.io.IOException;
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
     * 根据内涵段子URL,返回神评评论
     */
    public static Map<String, List> getGodMsg(String jsonURL) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(jsonURL);
        try {
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject().parseObject(result.toString().trim());

            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray array = data.getJSONArray("top_comments");

            Map<String, List> map = new HashMap<String, List>();
            List<Integer> hotGoods = new ArrayList<Integer>();
            List<String> hotContents = new ArrayList<String>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String good = obj.getString("digg_count");
                String content = obj.getString("text");

                if (good == null || good.length() < 1 || content == null || content.length() < 1) {
                    continue;
                }
                if (Integer.valueOf(good) <= 10) {
                    continue;
                }

                hotGoods.add(Integer.valueOf(good));
                hotContents.add(content);
            }
            map.put("hotGoods", hotGoods);
            map.put("hotContents", hotContents);
            return map;

        } catch (IOException e) {
            logger.error("get the " + jsonURL + "error", e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("get the " + jsonURL + "error", e);
            }
        }
    }


    public static void main(String[] a){
//        HttpUtil.getRandomUser("http://joke2.oupeng.com/comment/joke/user");
    }




}
