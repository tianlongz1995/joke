package com.oupeng.joke.back.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpComment {

    private static final Logger logger = LoggerFactory.getLogger(HttpComment.class);

    /**
     * 根据内涵段子URL,返回神评评论
     */
    public static Map<String, List> getNeiHanGodMsg(String jsonURL) {

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
            if(!CollectionUtils.isEmpty(array)) {
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
                    if (content.length() > 500) {
                        continue;
                    }
                    hotGoods.add(Integer.valueOf(good));
                    hotContents.add(content);
                }
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

    /**
     * 根据遨游哈哈URL,返回神评评论
     */
    public static Map<String, List> getHHMXGodMsg(String jsonURL) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(jsonURL);
        try {
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject().parseObject(result.toString().trim());

            JSONArray arrayList = jsonObject.getJSONArray("list");

            Map<String, List> map = new HashMap<String, List>();
            List<Integer> hotGoods = new ArrayList<Integer>();
            List<String> hotContents = new ArrayList<String>();
            if(!CollectionUtils.isEmpty(arrayList)) {
                for (int i = 0; i < arrayList.size(); i++) {

                    JSONArray array = arrayList.getJSONArray(i);
                    JSONObject obj = array.getJSONObject(0);
                    Integer good = (int) (30 * Math.random()) + 10;
                    String content = obj.getString("content");

                    if (content == null || content.length() < 1) {
                        continue;
                    }
                    if (content.length() > 500) {
                        continue;
                    }
                    hotGoods.add(good);
                    hotContents.add(content);
                }
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

//    public static void main(String[] args){
//        String jsonURL = "http://www.haha.mx/mobile_read_api.php?r=mobile_comment&jid=2530050&page=1&offset=10&order=light";
//        getHHMXGod(jsonURL);
//
//    }


}
