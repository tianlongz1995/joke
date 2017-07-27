package com.oupeng.joke.spider.service;


import com.oupeng.joke.spider.domain.ImageDto;
import com.oupeng.joke.spider.utils.Im4JavaUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
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

    /**
     * 获取图片的格式
     */
    public static String getPicFormat(String imgUrl) {

        ImageInputStream iis = null;
        String result = null;
        try {

            URL url = new URL(imgUrl);
            URLConnection con = url.openConnection();

            iis = ImageIO.createImageInputStream(con.getInputStream());
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {
                String picFormat = iter.next().getFormatName();

                if (picFormat.equals("gif")) {
                    result = "gif";
                } else if (picFormat.equals("jpg") || picFormat.equals("jpeg") || picFormat.equals("png")) {
                    result = "jpg";
                }
            }
        } catch (Exception e) {
            logger.error("getPicFormat failed " + e.getMessage(), e);
            return null;
        } finally {
            //关闭所有链接
            try {
                if (iis != null) {
                    iis.close();
                }
            } catch (IOException e) {
                logger.error("getPicFormat iis close failed " + e.getMessage(), e);
                return null;
            }
        }
        return result;
    }

    @PostConstruct
    public void initPath() {
        String c = env.getProperty("img.cdn.path");

        if (StringUtils.isNotBlank(c)) {
            cdnImagePath = c;
        }

    }

    public ImageDto downloadImg(String imgUrl) {
        if(!imgUrl.startsWith("http")){
            imgUrl = "http:" + imgUrl;
        }
        OutputStream os = null;
        InputStream is = null;
        int random = new Random().nextInt(3000);
        ImageDto image = new ImageDto();
        File dir;
        //新的文件名
        String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR_STR;
        String imgType = "";
        //是否为gif
        boolean isGif = false;
        try {
            dir = new File(cdnImagePath + random  + "/");
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            URL url = new URL(imgUrl);
            URLConnection con = url.openConnection();
            //  java.io.IOException: Server returned HTTP response code: 403 for URL
            //  因为服务器的安全设置不接受Java程序作为客户端访问
            //  解决方案：设置User Agent
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //  判断图片的格式
            String result = getPicFormat(imgUrl);
            if (result == null) {
                return null;
            }
            if (result.equals("gif")) {
                imgType = "gif";
                isGif = true;
            } else if (result.equals("jpg")) {
                imgType = "jpg";
            }
            image.setImgType(imgType);


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
            logger.error("download image failed "+e.getMessage(), e);
            return null;
        } finally {
            //关闭所有链接
            try {
                if (null != is && null != os) {
                    is.close();
                    os.close();
                }
            } catch (IOException e) {
                logger.error("download image关闭链接异常"+e.getMessage(), e);
            }
        }

        String cdnUrl = cdnImagePath + random + "/" + newFileName;

        int[] widthHeight = Im4JavaUtils.getWidthHeight(cdnUrl);
        if (widthHeight == null) {
            return null;
        }

//        //图片宽和高 取原图75%
//        Double width = widthHeight[0] * 0.75;
//       int wid = width.intValue();
//        Double height = widthHeight[1] * 0.75;
//        int he = height.intValue();

        //压缩
//        boolean isSuccess = Im4JavaUtils.resizeImage(cdnUrl, cdnUrl, widthHeight[0], widthHeight[1], false);
//        if (isSuccess){ //压缩成功
//            image.setWidth(wid);
//            image.setHeight(he);
//        }else {

        image.setWidth(widthHeight[0]);

        image.setHeight(widthHeight[1]);
//        }

        if (isGif) {
            String imgName = handleImg(cdnUrl);
            //动图切图是否成功
            if (StringUtils.isNotBlank(imgName)) {
                image.setImgUrl(random + "/" + imgName);
            }
        } else {
            image.setImgUrl(random + "/" + newFileName);
        }
        return image;
    }

    /**
     * gif取第一帧处理
     *
     * @param imgUrl
     * @return
     */
    private String handleImg(String imgUrl) {
        if (StringUtils.isNotBlank(imgUrl)) {
            //静态图
            String target = StringUtils.replace(imgUrl, "gif", "jpg");
            //获取第一帧
            boolean isSuccess = Im4JavaUtils.getGifOneFrame(imgUrl, target, 0);
            if (isSuccess) {
                return FilenameUtils.getName(target);
            }
        }
        return null;
    }

}
