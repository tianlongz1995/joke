package com.oupeng.joke.spider.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 根据json URL,获取json数据（get方式）
     */
    public static JSONObject getAllJsonMsg(String url) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject().parseObject(result.toString().trim());

            return jsonObject;
        } catch (IOException e) {
            logger.error("get the " + url + "error", e);
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
                logger.error("get the " + url + "error", e);
            }
        }
    }


    public static void main(String[] a) {


//        String pageURL = "http://neihanshequ.com/p61863627690/";
//        String str = pageURL.substring("http://neihanshequ.com/p".length(), pageURL.length() - 1);
//        String jsonURL = "http://neihanshequ.com/m/api/get_essay_comments/?group_id=" + str + "&app_name=neihanshequ_web&offset=0";
//
//
//        List<String> hotGoods = new ArrayList<String>();
//        List<String> hotContents = new ArrayList<String>();
//
//        JSONObject retjson = HttpUtil.getAllJsonMsg(jsonURL);
//        JSONObject data = (JSONObject) retjson.get("data");
//        JSONArray array = data.getJSONArray("top_comments");
//
//        for (int i = 0; i < array.size(); i++) {
//            JSONObject obj = array.getJSONObject(i);
//            String good = obj.getString("digg_count");
//            String content = obj.getString("text");
//
//            if (good == null || good.length() < 1 || content == null || content.length() < 1) continue;
//            if (Integer.valueOf(good) <= 10) continue;
//
//            hotGoods.add(good);
//            hotContents.add(content);
//        }
//        int i = 0;


    }


}
