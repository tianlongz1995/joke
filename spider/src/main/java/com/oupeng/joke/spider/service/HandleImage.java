package com.oupeng.joke.spider.service;


import com.oupeng.joke.spider.utils.ImageUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.UUID;

/**
 * Created by zongchao on 2017/3/8.
 */
@Component
public class HandleImage {

    private static final Logger logger = LoggerFactory.getLogger(HandleImage.class);


    //图片路径
    private String cdnImagePath = "/data01/images/";


    @Autowired
    private Environment env;


    @PostConstruct
    public void initPath() {
        String c = env.getProperty("img.cdn.path");

        if (StringUtils.isNotBlank(c)) {
            cdnImagePath = c;
        }

    }

    public String downloadImg(String imgUrl) {
        //服务器上的图片地址
        String realUrl = null;
        OutputStream os = null;
        InputStream is = null;
        int random = new Random().nextInt(3000);
        File dir;
        //新的文件名
        String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR_STR;
        String imgType = "";

        try {

            dir = new File(cdnImagePath + random + "/");
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            //文件类型
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
            String path = FilenameUtils.concat(dir.getCanonicalPath(), newFileName);
            //保存路径
            os = new FileOutputStream(path);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (Exception e) {
            logger.error("download image failed ", e);
        } finally {
            //关闭所有链接
            try {
                if (null != is && null != os) {
                    is.close();
                    os.close();
                }
            } catch (IOException e) {
                logger.error("download image failed ", e);
            }
        }
        String cdnUrl = cdnImagePath + random + "/" + newFileName;
        String imgName = handleImg(cdnUrl, false);
        if (StringUtils.isNotBlank(imgName)) {
            return random + "/" + imgName;
        }
        return null;
    }

    private String handleImg(String imgUrl, boolean isCrop) {
        if (StringUtils.isNotBlank(imgUrl)) {
            String imgurl = ImageUtil.handleImg(imgUrl, isCrop);
            if (imgurl != null && imgurl.length() != 0) {
                return imgurl;
            }
        }
        return null;
    }
}
