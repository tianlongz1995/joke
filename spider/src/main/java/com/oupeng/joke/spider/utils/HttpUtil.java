package com.oupeng.joke.spider.utils;

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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

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

    /**
     * 根据遨游哈哈URL,返回神评评论
     */
    public static Map<String, List> getHHmxMsg(String pageURL) {

        Map<String, List> map = new HashMap<String, List>();
        try {
            String str = pageURL.substring("http://www.haha.mx/joke/".length(), pageURL.length());
            String url = "http://www.haha.mx/front_api.php?r=get_comments";
            String params = "jid="+str+"&page=1&offset=10&order=light";

            String cont = HttpUtil.httpPost(url, params);
            JSONObject jsonObject = new JSONObject().parseObject(cont.trim());
            JSONArray jsonArray = jsonObject.getJSONArray("comments");

            List<Integer> hotGoods = new ArrayList<Integer>();
            List<String> hotContents = new ArrayList<String>();
            if (!CollectionUtils.isEmpty(jsonArray)) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONArray array = jsonArray.getJSONArray(i);
                    JSONObject obj = array.getJSONObject(0);

                    String good = obj.getString("light");
                    String content = obj.getString("content");

                    if (good == null || good.length() < 1 || Integer.valueOf(good) <= 10 || content == null || content.length() < 1) {
                        continue;
                    }

                    content = StringUtil.removeSpecial(content);
                    if (content.length() > txtLimitLength) {
                        continue;
                    }

                    hotGoods.add(Integer.valueOf(good));
                    hotContents.add(content);
                }
            }
            map.put("hotGoods", hotGoods);
            map.put("hotContents", hotContents);

        }catch (Exception e){
            logger.error("重爬joke神评,连接["+pageURL+"]异常"+e.getMessage(),e);
            return null;
        }
        return map;
    }

}
