package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ChoiceMapper;
import com.oupeng.joke.domain.Choice;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
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

    private String uploadPath;
    private String showPath;
    private String realPath;
    private String cropPath;

    @PostConstruct
    public void initPath() {
        String u = env.getProperty("upload_image_path");
        String s = env.getProperty("show_image_path");
        String r = env.getProperty("img.real.server.url");
        String c = env.getProperty("remote.crop.img.server.url");
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
            cropPath = c;
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
    public boolean addChoice(String title, String content, String image, Integer width, Integer height) {
        String newImg = handleImg(image);
        if (StringUtils.isBlank(newImg)) {
            return false;
        }
        choiceMapper.addChoice(title, content, newImg, width, height);
        return true;
    }

    public String handleImg(String imgUrl) {
        if (StringUtils.isNotBlank(imgUrl)) {
            ImgRespDto imgRespDto = HttpUtil.handleImg(cropPath, imgUrl, false);
            if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
                return imgRespDto.getImgUrl();
            }
        }
        return null;
    }

    /**
     * 下载精选内容中的非服务器上的图片
     *
     * @param imgUrlList
     * @return
     */
    public List<String> downloadImg(List<String> imgUrlList) {
        //服务器上的图片地址
        List<String> realUrl = new ArrayList<>();
        OutputStream os = null;
        InputStream is = null;
        for (String imgUrl : imgUrlList) {
            //新的文件名
            String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR_STR;
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
                String path = FilenameUtils.concat(uploadPath, newFileName);
//                String path ="C:/Users/rainy/joke/"+newFileName;
                //保存路径
                os = new FileOutputStream(path);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //关闭所有链接
                try {
                    if (null != is && null != os) {
                        is.close();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //服务器上图片地址
            realUrl.add(showPath + newFileName);
        }

        List<String> realUrl2 = new ArrayList<>();
        for (String u : realUrl) {
            String url = handleImg(u);
            if (StringUtils.isNotBlank(url)) {
                realUrl2.add(realPath + url);
            }
        }
        return realUrl2;
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
     * 获取 非服务器图片地址
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

    public boolean updateChoice(Integer id, String title, String content, String image, Integer width, Integer height) {
        String newImg;
        //重新上传的图片
        if (image.startsWith(showPath)) {
            newImg = handleImg(image);
            if (StringUtils.isEmpty(image)) {
                return false;
            }
        } else {
            //已经上传的图片
            newImg = image.replace(realPath, "");
        }
        choiceMapper.updateChoice(id, title, content, newImg, width, height);
        return true;
    }


    /**
     * 更新精选状态
     *
     * @param id
     * @param status
     */
    public void updateChoiceStatus(Integer id, Integer status) {
        Choice choice = choiceMapper.getChoiceById(id);
        String choiceKey = JedisKey.STRING_JOKE + id;
        //精选id列表，缓存key
        String choiceListKey = JedisKey.JOKE_CHANNEL + 4;
        // 下线删除缓存
        if (status == 0) {
            jedisCache.del(choiceKey);
            jedisCache.zrem(choiceListKey, Integer.toString(id));
        } else {
            //增加缓存 - 上线
            choice.setType(3);
            choice.setImg(choice.getImg());
            jedisCache.set(choiceKey, JSON.toJSONString(choice));
            jedisCache.zadd(choiceListKey, System.currentTimeMillis(), id.toString());
        }
        choiceMapper.updateChoiceStatus(id, status);
    }
}
