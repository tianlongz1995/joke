package com.oupeng.joke.back.task;

import com.oupeng.joke.back.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * <pre>
 * 临时图片删除任务
 * 在back.properties文件中
 * upload_image_path是用户手动上传段子图片、动图的临时目录
 * 白天运营人员上传段子后,临时文件不会立即删除
 * 这个定时任务就是凌晨运营人员不上传图片时清除历史文件的
 * 上传完成的图片会存储到CDN的回源目录【img.cdn.path】中
 * 所有删除临时文件不会对正常图片有任何影响
 * </pre>
 */
@Component
public class UploadTempImageDeleteTask {
    private static final Logger logger = LoggerFactory.getLogger(UploadTempImageDeleteTask.class);

    @Autowired
    private Environment env;
    /**
     * 上传图片的临时目录
     */
    private String uploadImagePath = "/nh/java/back/resources/image/";

    @PostConstruct
    public void init() {
        String uploadPath = env.getProperty("upload_image_path");
        if (StringUtils.isNoneBlank(uploadPath)) {
            uploadImagePath = uploadPath;
        } else {
            logger.error("getProperty(\"upload_image_path\") is null:{}", uploadPath);
        }
    }


    /**
     * 10分钟同步一次频道广告配置缓存
     */
    @Scheduled(cron = "1 1 1 * * ?")
    public void execute() {
        long start = System.currentTimeMillis();
        logger.info("临时图片删除任务启动...");
        File file = new File(uploadImagePath);
        File[] tempList = file.listFiles();
        int delSize = tempList.length;
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                tempList[i].delete();
                logger.info("删除临时图:[{}]",tempList[i]);
            }
            if (tempList[i].isDirectory()) {
                logger.info("文件夹：" + tempList[i]);
            }
        }
        long end = System.currentTimeMillis();
        logger.info("临时图片删除任务执行完成: 删除文件:[{}]条, 耗时:[{}]", delSize, FormatUtil.getTimeStr(end - start));
    }
}
