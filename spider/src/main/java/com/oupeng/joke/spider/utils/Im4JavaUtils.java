package com.oupeng.joke.spider.utils;

import org.im4java.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class Im4JavaUtils {

    private static final Logger logger = LoggerFactory.getLogger(Im4JavaUtils.class);

    /**
     * 是否使用 GraphicsMagick
     **/
    private static final boolean USE_GRAPHICS_MAGICK_PATH = true;
    /**
     * ImageMagick 安装目录
     **/
    private static final String IMAGE_MAGICK_PATH = "F:/ImageMagick-7.0.5-Q16";
    /**
     * GraphicsMagick 安装目录
     **/
    private static final String GRAPHICS_MAGICK_PATH = "F:/GraphicsMagick";

    /**
     * 获取 ImageCommand
     *
     * @param comm 命令类型（convert, identify）
     * @return
     */
    private static ImageCommand getImageCommand(String comm) {
        ImageCommand cmd = null;
        if ("convert".equalsIgnoreCase(comm)) {
            cmd = new ConvertCmd(USE_GRAPHICS_MAGICK_PATH);
        } else if ("identify".equalsIgnoreCase(comm)) {
            cmd = new IdentifyCmd(USE_GRAPHICS_MAGICK_PATH);
        } // else if....

        if (cmd != null && System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            cmd.setSearchPath(USE_GRAPHICS_MAGICK_PATH ? GRAPHICS_MAGICK_PATH : IMAGE_MAGICK_PATH);
        }
        return cmd;
    }


    /**
     * 创建目录
     *
     * @param path
     */
    private static void createDirectory(String path) {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        file.getParentFile().mkdirs();
    }


    /**
     * 获取GIF图片一帧图片 - 同步执行
     *
     * @param src
     * @param target
     * @param frame
     * @throws Exception
     */
    public static boolean getGifOneFrame(String src, String target, int frame) {
        if (!src.endsWith(".gif")) {
            return false;
        }
        try {
            createDirectory(target);
            IMOperation op = new IMOperation();
            op.addImage(src + "[" + frame + "]");
            op.addImage(target);
            ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
            cmd.setAsyncMode(false);
            cmd.run(op);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取GIF图片一帧图片 - 异步执行
     *
     * @param src
     * @param target
     * @param frame
     * @throws Exception
     */
    public static boolean getGifOneFrameAsyncMode(String src, String target, int frame) {
        if (!src.endsWith(".gif")) {
            return false;
        }
        try {
            createDirectory(target);
            IMOperation op = new IMOperation();
            op.addImage(src + "[" + frame + "]");
            op.addImage(target);
            ConvertCmd cmd = (ConvertCmd) getImageCommand("convert");
            cmd.setAsyncMode(true);
            cmd.run(op);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获得图片宽度 高度
     *
     * @param src
     * @return
     */
    public static int[] getWidthHeight(String src) {

        File file = null;
        BufferedImage bufferedImage = null;
        try {
            file = new File(src);
            bufferedImage = ImageIO.read(file);
            return new int[]{bufferedImage.getWidth(), bufferedImage.getHeight()};
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


}