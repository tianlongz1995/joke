package com.oupeng.joke.front.controller;

import com.oupeng.joke.domain.Feedback;
import com.oupeng.joke.domain.log.ClickLog;
import com.oupeng.joke.domain.log.ImprLog;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.front.service.JokeService;
import com.oupeng.joke.front.util.Constants;
import com.oupeng.joke.front.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/joke")
public class JokeController {
	
	private static final Logger impr = LoggerFactory.getLogger("impr");
	private static final Logger clk = LoggerFactory.getLogger("clk");
	
    @Autowired
    private JokeService jokeService;
    /**
     * 获取渠道配置接口
     * @param did	渠道编号
     * @return
     */
    @RequestMapping(value = "/getDistributorConfig", produces = {"application/json"})
    @ResponseBody
    public Result getDistributorConfig(@RequestParam(value="did",required=true)Integer did,
    		HttpServletRequest request){
    	String uid = CookieUtil.getCookie(request);
    	impr.info(new ImprLog(did, null, uid, Constants.IMPR_LOG_TYPE_DISTRIBUTOR).toString());
        return jokeService.getDistributorConfig(String.valueOf(did));
    }
    /**
     * 踩赞接口
     * @param id	文章编号
     * @param type	类型: 1：赞；2：踩
     * @return
     */
    @RequestMapping(value = "/stepLike", produces = {"application/json"})
    @ResponseBody
    public Result stepLike(@RequestParam(value="id",required=true)Integer id, 
    		@RequestParam(value="type",required=true)Integer type,
    		HttpServletRequest request){
    	String uid = CookieUtil.getCookie(request);
    	clk.info(new ClickLog(id, uid, type).toString());
        jokeService.stepLike(id, type);
        return new Success();
    }
    /**
     * 反馈
     * @param feedback	反馈
     * @return
     */
    @RequestMapping(value = "/feedback", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Result feedback(@RequestParam(value="feedback",required=true)Feedback feedback){
        jokeService.feedback(feedback);
        return new Success();
    }
}
