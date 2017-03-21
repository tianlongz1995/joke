package com.oupeng.joke.spider.utils;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by chaoz on 2017/3/5.
 */
public class ImageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private static final String JPG = "jpg";
    private static final String GIF = "gif";

    /**
     * 获取动图第一帧
     *
     * @param srcUrl
     * @return
     */
    public static String handleImg(String srcUrl) {

        //静态图
        String dest = StringUtils.replace(srcUrl, GIF, JPG);
        //处理成功
        if (StringUtils.isNotBlank(dest) && handleImg(srcUrl, dest)) {
            return FilenameUtils.getName(dest);
        }
        return null;
    }

    private static boolean handleImg(String src, String dest) {
        FileInputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            //要操作的图片
            in = new FileInputStream(src);

            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            //截取第一张图
            BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(bos.toByteArray(), 0, bos.size()));
            ImageIO.write(bimage, "png", new File(dest));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            close(in, bos);
        }
        return false;
    }

    private static void close(Closeable in, Closeable out) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}