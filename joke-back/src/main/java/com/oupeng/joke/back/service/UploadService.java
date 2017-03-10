package com.oupeng.joke.back.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadService {

	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);
	
	@Autowired
	private Environment env;

    private String uploadImagePath = "/nh/java/back/resources/image/";

    @PostConstruct
    private void init(){
        String uploadPath = env.getProperty("upload_image_path");
        if (StringUtils.isNoneBlank(uploadPath)) {
            uploadImagePath = uploadPath;
        } else {
            logger.error("getProperty(\"upload_image_path\") is null:{}", uploadPath);
        }
    }

	public String copyImg(MultipartFile img){
		String suffix = FilenameUtils.getExtension(img.getOriginalFilename());
        String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString()
                + FilenameUtils.EXTENSION_SEPARATOR_STR + suffix;
        String path = FilenameUtils.concat(uploadImagePath, newFileName);
        String url = null;
        try {
			img.transferTo(new File(path));
			url = env.getProperty("show_image_path") + newFileName;
		} catch (IllegalStateException | IOException e) {
			logger.error("image upload error!",e);
		}
        return url;
	}
}
