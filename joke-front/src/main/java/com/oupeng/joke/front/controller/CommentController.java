package com.oupeng.joke.front.controller;

import com.oupeng.joke.domain.Result;
import com.oupeng.joke.front.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by java_zong on 2017/4/19.
 */
//@Controller
//@RequestMapping(value = "/comment")
public class CommentController {

//    @Autowired
//    private CommentService commentService;
//
//    @RequestMapping(value="/likeComment")
//    @ResponseBody
//    public Result likeComment(@RequestParam(value="id") Integer id,
//                              @RequestParam(value = "uid",required = false) Integer uid){
//        if(commentService.likeComment(id,uid)){
//            return new Result(0);
//        }else {
//            return new Result(1);
//        }
//    }
}
