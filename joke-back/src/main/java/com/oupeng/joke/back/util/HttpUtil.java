package com.oupeng.joke.back.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
	 * GET请求
	 *
	 * @param requestUrl 请求地址
	 * @return
	 */
	public static String httpGet(String requestUrl) {

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
			httpUrlConn.setUseCaches(false);  //不允许缓存
			httpUrlConn.setConnectTimeout(3000);//连接超时 单位毫秒
			httpUrlConn.setReadTimeout(3000);//读取超时 单位毫秒
			httpUrlConn.setDoInput(true);

			if (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
				bufferedReader.close();
			}

		} catch (Exception e) {
			logger.error("GET请求[" + requestUrl + "]异常" + e.getMessage(), e);
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
				logger.error("关闭[" + requestUrl + "]连接异常" + e.getMessage(), e);
			}
		}
		return buffer.toString();
	}

	/**
	 * POST请求获取数据
	 *
	 * @param requestUrl 请求地址
	 * @param params     参数(可多个)
	 * @return
	 */
	public static String httpPost(String requestUrl, String params) {
		StringBuffer buffer = new StringBuffer();

		//建立连接
		HttpURLConnection httpConn = null;
		DataOutputStream dos = null;
		BufferedReader responseReader = null;

		try {
			URL url = new URL(requestUrl);
			httpConn = (HttpURLConnection) url.openConnection();
			//设置参数
			httpConn.setDoOutput(true);   //需要输出
			httpConn.setDoInput(true);   //需要输入
			httpConn.setUseCaches(false);  //不允许缓存
			httpConn.setRequestMethod("POST");   //设置POST方式连接
			httpConn.setConnectTimeout(3000);//连接超时 单位毫秒
			httpConn.setReadTimeout(3000);//读取超时 单位毫秒

			//设置请求属性
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//连接[httpConn.getOutputStream()会自动connect]
			httpConn.connect();

			//建立输入流，向指向的URL传入参数
			dos = new DataOutputStream(httpConn.getOutputStream());
			dos.writeBytes(params);
			dos.flush();
			dos.close();

			//获得响应状态
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String readLine = null;
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					buffer.append(readLine);
				}
				responseReader.close();
			}
		} catch (Exception e) {
			logger.error("post请求[" + requestUrl + "]异常" + e.getMessage(), e);
			return null;
		} finally {
			// 释放资源
			try {
				if (responseReader != null) {
					responseReader.close();
				}
				if (dos != null) {
					dos.close();
				}
				if (httpConn != null) {
					httpConn.disconnect();
				}
			} catch (IOException e) {
				logger.error("关闭[" + requestUrl + "]连接异常" + e.getMessage(), e);
			}
		}
		return buffer.toString();
	}

}
