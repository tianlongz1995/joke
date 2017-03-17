package com.oupeng.joke.spider.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by chaoz on 2017/3/5.
 */
public class ImageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);


    private static final String JPG = "jpg";
    private static final String GIF = "gif";

    /**
     * cdn存放图片
     *
     * @param srcUrl
     * @param isCrop
     * @return
     */
    public static String handleImg(String srcUrl, boolean isCrop) {

        //名称
        String baseName = FilenameUtils.getBaseName(srcUrl);

        String dest = null;

        if (!isCrop) {
            //不切图
            dest = FilenameUtils.getFullPath(srcUrl) + baseName + "." + JPG;
            handleImg(srcUrl, dest, "X1000");
            return FilenameUtils.getName(dest);
        }
        return null;
    }

    private static void handleImg(String src, String dest, String type) {

        double wd = 0, he = 0;
        int w = 0, h = 0;
        try {
            File srcFile = new File(src);
            File destFile = new File(dest);
            BufferedImage bufImg = ImageIO.read(srcFile);

            //获取缩放比例
            if (type.equalsIgnoreCase("X200")) {
                w = 200;
                h = 200;
            } else {
                w = bufImg.getWidth();
                h = bufImg.getHeight();
            }
            wd = w * 1.0 / bufImg.getWidth();
            he = h * 1.0 / bufImg.getHeight();
            //设置缩放目标图片模板
            Image itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);

            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wd, he), null);
            itemp = ato.filter(bufImg, null);
            //写入缩减后的图片
            ImageIO.write((BufferedImage) itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
        } catch (Exception e) {
            logger.error("upload img to crop error !", e);
        }
    }
}