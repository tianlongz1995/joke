package com.oupeng.joke.back.util;

import com.oupeng.joke.domain.Image;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.imageio.plugins.gif.GIFImageWriter;
import com.sun.imageio.plugins.gif.GIFImageWriterSpi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);


//	public static void main(String[] args) {
//        ImageUtils.getGifOneFrame("/nh/logs/c.gif", 5);
//	}

    /**
     * 获取GIF图片一帧图片
     * @param src       GIF图地址
     * @param target    目标地址
     * @param frame     第几帧
     * @return
     */
    public static boolean getGifOneFrame(String src, String target, int frame) {
        FileImageInputStream in = null;
        FileImageOutputStream out = null;
        try {
            in = new FileImageInputStream(new File(src));
            ImageReaderSpi readerSpi = new GIFImageReaderSpi();
            GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
            gifReader.setInput(in);
            int num = gifReader.getNumImages(true);
            if (num > frame) {
                ImageWriterSpi writerSpi = new GIFImageWriterSpi();
                GIFImageWriter writer = (GIFImageWriter) writerSpi.createWriterInstance();
                for (int i = 0; i < num; i++) {
                    if (i == frame) {
                        File newfile = new File(target);
                        out = new FileImageOutputStream(newfile);
                        writer.setOutput(out);
                        writer.write(gifReader.read(i));
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            close(in, out);
        }
    }

    /**
     * 复制文件
     * @param src
     * @param target
     * @return
     */
    public static boolean copyImageToCDN(String src, String target) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File srcFile = new File(src);
            File targetFile = new File(target);
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            close(in, out);
        }
    }

    /**
     * 获取图片宽高
     * @param file
     * @return
     */
    public static void getImgWidthAndHeight(File file, Image image) {
        InputStream is = null;
        BufferedImage src ;
        try {
            if(image == null){
                image = new Image();
            }
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            image.setWidth(src.getWidth());
            image.setHeight(src.getHeight());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(is, null);
        }
    }

    /**
     * 获取图片宽高
     * @param file
     * @return
     */
    public static Image getImgWidthAndHeight(File file) {
        InputStream is = null;
        BufferedImage src ;
        Image image = new Image();
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            image.setWidth(src.getWidth());
            image.setHeight(src.getHeight());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(is, null);
        }
        return image;
    }

    /**
     * 获取图片宽度
     * @param file
     * @return
     */
    public static int getImgWidth(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = 0;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(); // 得到源图宽
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(is, null);
        }
        return ret;
    }


    /**
     * 获取图片高度
     * @param file  图片文件
     * @return 高度
     */
    public static int getImgHeight(File file) {
        InputStream is = null;
        BufferedImage src;
        int ret = 0;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(); // 得到源图高
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            close(is, null);
        }
        return ret;
    }

    /**
     * 生成图片
     * @param src
     * @param target
     * @param format
     * @return
     */
    public static Image generateImage(String src, String target, String format, Image image) {
        try {
            if(image == null){
                image = new Image();
            }
            File input = new File(src);
            BufferedImage bim = ImageIO.read(input);
            image.setWidth(bim.getWidth());
            image.setHeight(bim.getHeight());
            File output = new File(target);
            ImageIO.write(bim, format, output);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return image;
    }



    private static void close(Closeable in, Closeable out) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
        }
    }
}
