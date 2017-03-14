package com.oupeng.joke.spider.utils;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/8.
 */
public class HandleImage {
    private static final String uploadPath = "E:/oupeng/joke/joke-back/target/joke-back-2.0/resources/image";
    private static final String showPath = "http://localhost:8080/joke-back/resources/image/";
    private static final String realPath = "http://localhost:8080/joke-back/resources/image/";
    private static final String cropRealPath = "E:/oupeng/joke/joke-back/target/joke-back-2.0/resources/image/";

    public static String downloadImg(String imgUrl) {
        //服务器上的图片地址
        String realUrl = null;
        OutputStream os = null;
        InputStream is = null;

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

            String url = handleImg(showPath + newFileName, false);
            if (StringUtils.isNotBlank(url)) {
                return showPath+url;
            }
       return null;
    }

    public static  String handleImg(String imgUrl, boolean isCrop) {
        if (StringUtils.isNotBlank(imgUrl)) {
            String imgurl = ImageUtil.handleImg(cropRealPath, imgUrl, isCrop);
            if (imgurl != null && imgurl.length() != 0) {
                return imgurl;
            }
        }
        return null;
    }
}
