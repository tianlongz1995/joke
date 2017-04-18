package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by java_zong on 2017/4/18.
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController {
    private static final Logger logger= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;
}
