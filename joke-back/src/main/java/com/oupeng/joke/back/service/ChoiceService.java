package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ChoiceMapper;
import com.oupeng.joke.domain.Choice;
import com.oupeng.joke.domain.IndexItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rainy on 2017/1/4.
 */
@Service
public class ChoiceService {

    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private Environment env;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private IndexCacheFlushService indexCacheFlushService;

    /**
     * 统计精选总条数
     * @param status
     * @return
     */
    public Integer getChoiceListCount(Integer status){
        return choiceMapper.getChoiceListCount(status);
    }

    /**
     * 获取精选列表
     * @param status
     * @param offset
     * @param pageSize
     * @return
     */
    public List<Choice> getChoiceList(Integer status,Integer offset,Integer pageSize){
        return  choiceMapper.getBannerList(status, offset, pageSize);
    }

    /**
     * 添加精选
     * @param title
     * @param content
     */
    public void addChoice(String title,String content,String image){
         choiceMapper.addChoice(title,content,image);
    }

    /**
     * 下载精选内容中的非服务器上的图片
     * @param imgUrlList
     * @return
     */
    public List<String> downloadImg(List<String> imgUrlList) {
        //服务器上的图片地址
        List<String> realUrl= new ArrayList<>() ;
        OutputStream os = null;
        InputStream is = null;
        for(String imgUrl:imgUrlList) {
            //新的文件名
            String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString()+ FilenameUtils.EXTENSION_SEPARATOR_STR;
            try {
                //文件类型
                String imgType;
                URL url = new URL(imgUrl);
                URLConnection con = url.openConnection();
                //获取contentype,判断图片类型
                String cType = con.getContentType();
                if (cType.startsWith("image")) {
                    if (cType.contains("gif")) {
                        imgType = "gif";
                    } else {
                        imgType = "jpg";
                    }
                } else {
                    //文件类型不对
                    return realUrl;
                }
                // 输入流
                 is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                newFileName = newFileName + imgType;
                //TODO 修改图片上传地址
                String path = FilenameUtils.concat(env.getProperty("upload_image_path"), newFileName);
//                String path ="C:/Users/rainy/joke/"+newFileName;
                //保存路径
                 os = new FileOutputStream(path);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }

            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                //关闭所有链接
                try {
                    if(null != is && null != os) {
                        is.close();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //服务器上图片地址
            realUrl.add(env.getProperty("show_image_path") + newFileName);
        }
        return realUrl;
    }

    /**
     * 删除精选
     * @param id
     */
    public void delChoice(Integer id){
        choiceMapper.delChoice(id);
    }

    /**
     * 根据id获取精选
     * @param id
     * @return
     */
    public Choice getChoiceById(Integer id){
        return  choiceMapper.getChoiceById(id);
    }

    /**
     * 获取 非服务器图片地址
     * @param content
     * @return
     */
    public List<String> getImgUrl(String content){
        List<String> tempUrl = new ArrayList<>();
        //匹配图片地址
        String pattern = "(?<=src=\")[a-zA-z]+://[^\\s(?!\")$]*";
        Pattern pat = Pattern.compile(pattern);
        Matcher m = pat.matcher(content);
        String prefix = env.getProperty("show_image_path");
        while (m.find()) {
            //不是服务器上的图片地址
            if (!(m.group().startsWith(prefix))) {
                tempUrl.add(m.group());
            }
        }
        return tempUrl;
    }

    public void updateChoice(Integer id,String title,String content,String image){
      choiceMapper.updateChoice(id,title,content,image);
    }

    /**
     * 更新精选状态
     * @param id
     * @param status
     */
    public void updateChoiceStatus(Integer id,Integer status){
        Choice choice = choiceMapper.getChoiceById(id);
        String choiceKey = JedisKey.STRING_JOKE + id;
        //精选id列表，缓存key
        String choiceListKey = JedisKey.JOKE_CHANNEL + 4;
        // 下线删除缓存
        if (status == 0) {
            jedisCache.del(choiceKey);
            jedisCache.zrem(choiceListKey,Integer.toString(id));
        }else{
            //增加缓存 - 上线
            choice.setType(3);
            jedisCache.set(choiceKey, JSON.toJSONString(choice));
            jedisCache.zadd(choiceListKey, System.currentTimeMillis(), id.toString());
        }
         choiceMapper.updateChoiceStatus(id,status);
    }
}
