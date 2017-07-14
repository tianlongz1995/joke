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

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static int txtLimitLength = 200;

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
                    if (content.length() > txtLimitLength) {
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
}
