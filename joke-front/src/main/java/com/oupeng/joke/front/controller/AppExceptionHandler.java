package com.oupeng.joke.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AppExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void handleException(HttpServletRequest req, Exception e) {
        logger.error("请求[" + req.getRequestURI() +
                (StringUtils.isBlank(req.getQueryString()) ? "" : "?" + req.getQueryString()) + "]处理异常:" + e.getMessage());
        if(logger.isDebugEnabled()){
            logger.error(e.getMessage(), e);
        }
    }
}
