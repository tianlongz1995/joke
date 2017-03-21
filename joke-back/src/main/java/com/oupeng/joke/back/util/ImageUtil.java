package com.oupeng.joke.back.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

/**
 * Created by chaoz on 2017/3/5.
 */
public class ImageUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);


    private static final String JPG = "jpg";
    private static final String GIF = "gif";

    /*
     * src为源文件目录，dest为缩放后保存目录
     */
    public static String handleImg(String destUrl, String srcUrl, boolean isCrop) {

        if (!isCrop) {
            //不切图
            return FilenameUtils.getName(srcUrl);
        }
        //名称
        String baseName = FilenameUtils.getBaseName(srcUrl);
        //后缀
        String suffix = FilenameUtils.getExtension(srcUrl);

        //本机路径
        String src = destUrl + FilenameUtils.getName(srcUrl);
        String dest = null;

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

    /**
     *
     * @param src
     * @return
     */
    public static BufferedImage zoomImage(String src, int targetWidth) {
        BufferedImage result = null;
        try {
            File srcfile = new File(src);
            if (!srcfile.exists()) {
                logger.error("源文件不存在:{}", src);
                return null;
            }
            BufferedImage im = ImageIO.read(srcfile);
            /* 原始图像的宽度和高度 */
            int width = im.getWidth();
            int height = im.getHeight();
            //压缩计算
            float resizeTimes = (float)targetWidth / (float)width;
            BigDecimal bd  =   new  BigDecimal((double)resizeTimes);
            bd   =  bd.setScale(1 ,4); // (1:小数点位数, 2:表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
            resizeTimes   =  bd.floatValue();
//            float resizeTimes = 0.3f;  /*这个参数是要转化成的倍数,如果是1就是转化成1倍*/
            /* 调整后的图片的宽度和高度 */
            int toWidth = (int) (width * resizeTimes);
            int toHeight = (int) (height * resizeTimes);

            /* 新生成结果图片 */
            result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
            result.getGraphics().drawImage( im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        } catch (Exception e) {
            System.out.println("创建缩略图发生异常" + e.getMessage());
        }
        return result;
    }

    public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {
        try {
                /*输出到文件流*/
            FileOutputStream newimage = new FileOutputStream(fileFullPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);
                /* 压缩质量 */
            jep.setQuality(0.9f, true);
            encoder.encode(im, jep);
               /*近JPEG编码*/
            newimage.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean widthCutting(String source, String target, int width){
        return ImageUtil.writeHighQuality(ImageUtil.zoomImage(source, width), target);
    }

    public static void main(String[] args) {
        String src = "/Users/hushuang/Downloads/test2.jpg" ;
         /*这儿填写你存放要缩小图片的文件夹全地址*/
        String tar = "/Users/hushuang/Downloads/test2_200.jpg";
        /*这儿填写你转化后的图片存放的文件夹*/

//        ImageUtil.writeHighQuality(ImageUtil.zoomImage(src), tar);

        ImageUtil.widthCutting(src, tar, 200);
    }
}