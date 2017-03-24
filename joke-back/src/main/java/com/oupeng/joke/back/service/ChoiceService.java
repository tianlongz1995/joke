package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.Im4JavaUtils;
import com.oupeng.joke.back.util.ImageUtils;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ChoiceMapper;
import com.oupeng.joke.domain.Choice;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.Image;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rainy on 2017/1/4.
 */
@Service
public class ChoiceService {
    private static final Logger log = LoggerFactory.getLogger(ChoiceService.class);
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private Environment env;
    @Autowired
    private JedisCache jedisCache;

    private String uploadPath = "/nh/java/joke-back/resources/image/";
    private String showPath = "http://jokeback.bj.oupeng.com/resources/image/";
    private String realPath;
    private String cdnImagePath;

    @PostConstruct
    public void initPath() {
        String u = env.getProperty("upload_image_path");
        String s = env.getProperty("show_image_path");
        String r = env.getProperty("img.real.server.url");
        String c = env.getProperty("img.cdn.path");
        if(StringUtils.isNotEmpty(u)){
            uploadPath = u;
        }
        if(StringUtils.isNotEmpty(s)){
            showPath = s;
        }
        if(StringUtils.isNotEmpty(r)){
            realPath = r;
        }
        if(StringUtils.isNotEmpty(c)){
            cdnImagePath = c;
        }
    }

    /**
     * 统计精选总条数
     *
     * @param status
     * @return
     */
    public Integer getChoiceListCount(Integer status) {
        return choiceMapper.getChoiceListCount(status);
    }

    /**
     * 获取精选列表
     *
     * @param status
     * @param offset
     * @param pageSize
     * @return
     */
    public List<Choice> getChoiceList(Integer status, Integer offset, Integer pageSize) {
        List<Choice> list = choiceMapper.getBannerList(status, offset, pageSize);
        if (!CollectionUtils.isEmpty(list)) {
            for (Choice c : list) {
                if (StringUtils.isNotBlank(c.getImg())) {
                    c.setImg(realPath + c.getImg());
                }
            }
        }
        return list;
    }

    /**
     * 添加精选
     *
     * @param title
     * @param content
     */
    public boolean addChoice(String title, String content, String image, String publishTime) {
//      获取文件内容中的图片地址
        content = content.replaceAll("'", "#");
        List<String> sourceUrl = getImgUrl(content);
        List<String> realUrl;
        if (!CollectionUtils.isEmpty(sourceUrl)) {
            // 下载图片
            realUrl = downloadImg(sourceUrl);
            if (realUrl.size() == sourceUrl.size()) {
                for (int i = 0; i < realUrl.size(); i++) {
                    //替换图片地址
                    content = content.replace(sourceUrl.get(i), realUrl.get(i));
                }
            } else {
                return false;
            }
        }

//      处理封面图片
        Image img = handleStaticImg(image);
        if (img == null) {
            return false;
        }
        int bad  = 150 -(int)(Math.random()*150);
        int good = 500 +(int)(Math.random()*500);
        choiceMapper.addChoice(title, content, img.getPath(), img.getWidth(), img.getHeight(), publishTime, good, bad);
        return true;
    }

    /**
     * 处理静态图
     * @param img
     * @return
     */
    public Image handleStaticImg(String img) {
        String fileName = HttpUtil.getUrlImageFileName(img);
        String storeName;
        Image image = new Image();
        int random = new Random().nextInt(3000);
        try {
            File dir = null;
//          将图片从临时目录上传到CDN目录(images)
            if (fileName == null && fileName.length() < 1) {
                log.error("上传的图片类型段子没有图片信息:[{}]", img);
                return null;
            }
            dir = new File(cdnImagePath + random + "/");
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
//          所有静图都格式化为jpg
            storeName = fileName.substring(0, fileName.lastIndexOf("."));
            storeName = storeName + ".jpg";
//          生成图片并获得宽高
            ImageUtils.generateImageForIm4Java(uploadPath + fileName, dir.getCanonicalPath() + "/" + storeName, image);
            String resizeWidth600Name = storeName.replace(".jpg", "_w600.jpg");
            Im4JavaUtils.cropImageByWidth(uploadPath + fileName, dir.getCanonicalPath() + "/" + resizeWidth600Name, 600, 257, 50.0f);
            String resizeWidth200Name = storeName.replace(".jpg", "_w200.jpg");
            Im4JavaUtils.cropImageByWidth(uploadPath + fileName, dir.getCanonicalPath() + "/" + resizeWidth200Name, 200, 86, 50.0f);

            image.setPath(random + "/" + storeName);
            return image;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * <pre>
     * 下载精选内容中的非服务器上的图片
     * 1. 下载图片存储到CDN目录
     * 2. 返回CDN前缀+图片地址
     * @param imgUrlList
     * </pre>
     * @return
     */
    public List<String> downloadImg(List<String> imgUrlList) {
        //服务器上的图片地址
        List<String> realUrl = new ArrayList<>();
        OutputStream os = null;
        InputStream is = null;
        int random  = new Random().nextInt(3000);
        File dir;
        for (String imgUrl : imgUrlList) {
            try {
                dir = new File(cdnImagePath + random + "/");
                if(!dir.isDirectory()){
                    dir.mkdirs();
                }
    //            如果是服务器本地图片,就直接拷贝到CDN目录
                if(imgUrl.startsWith(showPath)){
                    String localImageName = HttpUtil.getUrlImageFileName(imgUrl);
                    boolean copyStatus = Im4JavaUtils.copyImage(uploadPath + localImageName, dir.getCanonicalPath() + "/" + localImageName);
                    if(copyStatus){
                        realUrl.add(realPath + random + "/" + localImageName);
                    }
                    continue;
                }


                //新的文件名
                String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR_STR;
                String imgType = "";

                //文件类型

                URL url = new URL(imgUrl);
                URLConnection con = url.openConnection();
                String cType = con.getContentType(); //获取contentype,判断图片类型
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
                //修改图片上传地址

                String path = FilenameUtils.concat(dir.getCanonicalPath(), newFileName);
                //保存路径
                os = new FileOutputStream(path);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                realUrl.add(realPath + random + "/" + newFileName);
                //                所有静图都格式化为jpg
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    if (null != is && null != os) {
                        is.close();
                        os.close();
                    }
                } catch (IOException e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
            //服务器上图片地址

        }
        return realUrl;
    }

    /**
     * 删除精选
     *
     * @param id
     */
    public void delChoice(Integer id) {
        choiceMapper.delChoice(id);
    }

    /**
     * 根据id获取精选
     *
     * @param id
     * @return
     */
    public Choice getChoiceById(Integer id) {
        Choice choice = choiceMapper.getChoiceById(id);
        if (choice != null && StringUtils.isNotBlank(choice.getImg())) {
            choice.setImg(realPath + choice.getImg());
        }
        return choice;
    }

    /**
     * 获取文件内容中的图片地址
     *
     * @param content
     * @return
     */
    public List<String> getImgUrl(String content) {
        List<String> tempUrl = new ArrayList<>();
        //匹配图片地址
        String pattern = "(?<=src=\")[a-zA-z]+://[^\\s(?!\")$]*";
        Pattern pat = Pattern.compile(pattern);
        Matcher m = pat.matcher(content);
        String prefix = realPath;
        while (m.find()) {
            //不是服务器上的图片地址
            if (!(m.group().startsWith(prefix))) {
                tempUrl.add(m.group());
            }
        }
        return tempUrl;
    }

    public boolean updateChoice(Integer id, String title, String content, String image,String publishTime) {
        content = content.replaceAll("'", "#");
        Image img = new Image();
        //重新上传的图片
        if (image.startsWith(showPath)) {
            img = handleStaticImg(image);
            if (img == null) {
                return false;
            }
        }
        choiceMapper.updateChoice(id, title, content, img.getPath(), img.getWidth(), img.getHeight(),publishTime);
        return true;
    }


    /**
     * 更新精选状态
     *
     * @param id
     * @param status
     */
    public String publishChoiceByTime(Integer id, Integer status) {
        String result = null;
        Choice choice = choiceMapper.getChoiceById(id);
        String choiceKey = JedisKey.STRING_JOKE + id;
        //精选id列表，缓存key
        String choiceListKey = JedisKey.JOKE_CHANNEL + 4;
        // 上线
        if (Constants.CHOICE_STATUS_VALID == status) {
            result = validChoice(choice, true);
        // 下线
        } else if (Constants.CHOICE_STATUS_PUBLISH != status) {
            //删除精选
            jedisCache.del(choiceKey);
            //删除列表中Id
            jedisCache.zrem(choiceListKey, Integer.toString(id));
            //删除推荐中Id
            jedisCache.srem(JedisKey.SET_RELATED_JOKE_IMG, String.valueOf(choice.getId()));
            log.info("删除精选缓存:[{}]", id);
        }
        if (result == null) {
            choiceMapper.updateChoiceStatus(id, status);
        }
        return result;
    }

    /**
     * 立即发布
     * @param id
     * @return
     */
    public String publishChoiceNow(Integer id){
        Choice choice = choiceMapper.getChoiceById(id);
        String choiceKey = JedisKey.STRING_JOKE + id;
        //精选id列表，缓存key
        String choiceListKey = JedisKey.JOKE_CHANNEL + 4;
        String result = validChoice(choice, false);
        if(null == result){
            //1 增加缓存
            choice.setType(3);
            if(choice.getCommentNumber()!=null){
                choice.setComment(new Comment(choice.getCommentNumber(),null,null,null));
            }
            jedisCache.set(choiceKey, JSON.toJSONString(choice));
            jedisCache.zadd(choiceListKey, System.currentTimeMillis(), choice.getId().toString());
            //2 更新发布状态
            choiceMapper.updateChoiceStatus(choice.getId(),3);
            //3 加入相关推荐
            jedisCache.sadd(JedisKey.SET_RELATED_JOKE_IMG, String.valueOf(choice.getId()));
        }
        return result;
    }


    public String validChoice(Choice choice,boolean validPublishTime){

        if(validPublishTime){
            if(choice.getPublishTime() == null){
                return "精选的发布时间不能为空";
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(choice.getPublishTime().compareTo(calendar.getTime()) < 0){
                return "发布时间最少要在下一个小时";
            }
        }
        if(StringUtils.isBlank(choice.getContent())){
            return "精选的内容不能为空";
        } else if (StringUtils.isBlank(choice.getImg())) {
            return "精选的封面图不能为空";
        }
        return null;
    }

    /**
     * 获取带发布的精选列表
     * @return
     */
    public  List<Choice> getChoiceForPublish(){
        return choiceMapper.getChoiceForPublish();
    }
}
