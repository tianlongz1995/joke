package com.oupeng.joke.back.util;

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
    /*
     * src为源文件目录，dest为缩放后保存目录
     */
    public static String handleImg(String destServerUrl, String destUrl, String srcServerUrl, String srcUrl) {

        String filename=srcServerUrl.substring(srcServerUrl.lastIndexOf("/") + 1);
        String[] fileName = filename.split("\\u002E");

        String src = srcUrl + filename;
        String dest = null;
        //缩放
        dest=destUrl+fileName[0]+"_X1000."+fileName[1];
        handleImg(src, dest, "X1000");
        if (fileName[1].contains("gif")) {
            dest=destUrl+fileName[0]+"_X200."+fileName[1];
            handleImg(src, dest, "X200");
        } else {
            dest=destUrl+fileName[0]+"_X500."+fileName[1];
            handleImg(src, dest, "X500");
        }
        //System.out.println(destServerUrl + dest.substring(dest.lastIndexOf("/")+1));
        return destServerUrl + dest.substring(dest.lastIndexOf("/")+1);

    }


    private static void handleImg(String src, String dest, String type) {

        double wd = 0, he = 0;
        int w = 0, h = 0;
        try {
            File srcFile = new File(src);
            File destFile = new File(dest);
            BufferedImage bufImg = ImageIO.read(srcFile);

            //获取缩放比例
            if (type.equalsIgnoreCase("X500")) {
                w = 320;
                h = bufImg.getHeight();
            } else if (type.equalsIgnoreCase("X200")) {
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
            e.printStackTrace();
        }
    }
}