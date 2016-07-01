package com.oupeng.joke.front.controller;

import com.oupeng.joke.domain.Feedback;
import com.oupeng.joke.domain.response.DistributorConfigResult;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.front.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JokeController {

    @Autowired
    private JokeService jokeService;
    /**
     * 获取渠道配置接口
     * @param did	渠道编号
     * @return
     */
    @RequestMapping(value = "/joke/getDistributorConfig")
    @ResponseBody
    public DistributorConfigResult getDistributorConfig(@RequestParam(value="did",required=true)Integer did){
        return jokeService.getDistributorConfig(did);
    }
    /**
     * 踩赞接口
     * @param id	文章编号
     * @param type	类型: 1：赞；2：踩
     * @return
     */
    @RequestMapping(value = "/joke/stepLike")
    @ResponseBody
    public Result stepLike(@RequestParam(value="id",required=true)Integer id, @RequestParam(value="type",required=true)Integer type){
        jokeService.stepLike(id, type);
        return new Success();
    }
    /**
     * 反馈
     * @param feedback	反馈
     * @return
     */
    @RequestMapping(value = "/joke/feedback", method = RequestMethod.POST)
    @ResponseBody
    public Result feedback(@RequestParam(value="feedback",required=true)Feedback feedback){
        jokeService.feedback(feedback);
        return new Success();
    }
}
