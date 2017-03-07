package com.oupeng.joke.back.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Administrator on 2017/3/5.
 */
public class ImageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);


    private static final String JPG = "jpg";
    private static final String GIF = "gif";

    /*
     * src为源文件目录，dest为缩放后保存目录
     */
    public static String handleImg(String destUrl, String srcUrl, boolean isCrop) {

        //名称
        String baseName = FilenameUtils.getBaseName(srcUrl);
        //后缀
        String suffix = FilenameUtils.getExtension(srcUrl);

        //本机路径
        String src = destUrl + FilenameUtils.getName(srcUrl);
        String dest = null;

        if (!isCrop) {
            //不切图
            dest = destUrl + baseName + "." + JPG;
            handleImg(src, dest, "X1000");
            return dest.substring(dest.lastIndexOf("/") + 1);
        }
        //静态图缩放
        dest = destUrl + baseName + "_X1000." + JPG;
        handleImg(src, dest, "X1000");
        dest = destUrl + baseName + "_X200." + JPG;
        handleImg(src, dest, "X200");
        //切图后删除原图片(不包括动图)
        if (!suffix.equalsIgnoreCase(GIF)) {
            File file = new File(src);
            if (file.exists()) {
                file.delete();
            }
        }

        return dest.substring(dest.lastIndexOf("/") + 1);

    }

//    public  static void main(String[] args){
//        handleImg("E:/oupeng/joke/joke-back/target/joke-back-2.0/resources/image/","http://localhost:8080/joke-back/resources/image/1488879731750_32963b0b-ad83-4e08-9e43-dfb03f710314.gif",false);
//    }

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
//            logger.error("upload img to crop error !", e);
            e.printStackTrace();
        }
    }
}