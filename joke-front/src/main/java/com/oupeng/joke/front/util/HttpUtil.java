package com.oupeng.joke.front.util;


import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.domain.Comment;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static Random random = new Random(3000);

    /**
     * 获取随机用户信息
     *
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
            if (jsonObject != null && jsonObject.get("avata") != null && jsonObject.get("nick") != null) {
                comment.setAvata(jsonObject.get("avata").toString());
                comment.setNick(jsonObject.get("nick").toString());
                comment.setUid(random.nextInt(2090) * 10000 + random.nextInt(20));
            }
            return comment;
        } catch (IOException e) {
            logger.error("upload img to crop error !", e);
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
     *
     * @param img
     * @return
     */
    public static String getUrlImageFileName(String img) {
        int pos = img.lastIndexOf("/");
        if (pos < 0) {
            return null;
        }
        return img.substring(pos + 1);
    }


}
