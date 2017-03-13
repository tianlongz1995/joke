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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/joke")
public class JokeController {
    /** 访问日志 */
	private static final Logger impr = LoggerFactory.getLogger("impr");
    /** 详情点击日志 */
	private static final Logger clk = LoggerFactory.getLogger("clk");
    /** 下拉刷新日志 */
    private static final Logger dfl = LoggerFactory.getLogger("dfl");
    /** 上拉刷新日志 */
    private static final Logger ufl = LoggerFactory.getLogger("ufl");
    /** 列表页图片展开日志 */
    private static final Logger iol = LoggerFactory.getLogger("iol");

    @Autowired
    private JokeService jokeService;
    /**
     * 获取渠道配置接口
     * @param did	渠道编号
     * @return
     */
    @RequestMapping(value = "/getDistributorConfig")
    @ResponseBody
    public Result getDistributorConfig(@RequestParam(value="did")Integer did,
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
    @RequestMapping(value = "/stepLike")
    @ResponseBody
    public Result stepLike(@RequestParam(value="id")Integer id,
                           @RequestParam(value="type")Integer type,
                           HttpServletRequest request){
    	String uid = CookieUtil.getCookie(request);
    	clk.info(new ClickLog(id, uid, type).toString());
        jokeService.stepLike(id, type);
        return new Success();
    }
    
    /**
     * 反馈
     * @param distributorId
     * @param channelId
     * @param type
     * @param content
     * @return
     */
    @RequestMapping(value = "/feedback")
    @ResponseBody
    public Result feedback(@RequestParam(value="distributorId")Integer distributorId,
                           @RequestParam(value="channelId")Integer channelId,
                           @RequestParam(value="type")Integer type,
                           @RequestParam(value="content")String content){
        Feedback feedback = new Feedback();
        feedback.setDistributorId(distributorId);
        feedback.setChannelId(channelId);
        feedback.setType(type);
        feedback.setContent(content);
        jokeService.feedback(feedback);
        return new Success();
    }

    /**
     * 段子列表页接口
     * @param distributorId 渠道编号
     * @param channelId     频道编号
     * @param topicId       主题编号
     * @param listType      列表类型: 0:普通频道，1：专题频道；2：推荐频道
     * @param actionType    动作类型: 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param start         开始位置: 分页参数
     * @param end           结束位置：分页参数
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Result jokeList(@RequestParam(value="did")Integer distributorId,
    		@RequestParam(value="cid")Integer channelId,
    		@RequestParam(value="tid",required=false)Integer topicId,
    		@RequestParam(value="lt")Integer listType,
            @RequestParam(value="at")Integer actionType,
    		@RequestParam(value="start",required=false,defaultValue="0")Long start,
    		@RequestParam(value="end",required=false,defaultValue="9")Long end,
    		HttpServletRequest request){
    	String uid = CookieUtil.getCookie(request);
		int type = Constants.IMPR_LOG_TYPE_LIST;
		if(start == 0 && end == 9){
			type = Constants.IMPR_LOG_TYPE_CHANNEL;
		}
    	impr.info(new ImprLog(distributorId, channelId, uid, type).toString());
    	if(actionType == 2){
            dfl.info(new ImprLog(distributorId, channelId, uid, type, null).toString());
        }
        if(actionType == 1){
            ufl.info(new ImprLog(distributorId, channelId, uid, type, null).toString());
        }
		if(start >= 0 && start <= end  && end - start < 20 ){
			return jokeService.getJokeList(distributorId, channelId,topicId, listType, start, end, actionType);

		}
        return new Success(null);
    }

    /**
     * 段子详情页接口
     * @param distributorId
     * @param channelId
     * @param topicId
     * @param listType
     * @param jokeId
     * @param request
     * @return
     */
    @RequestMapping(value = "/item")
    @ResponseBody
    public Result joke(@RequestParam(value="did")Integer distributorId,
    		@RequestParam(value="cid")Integer channelId,
    		@RequestParam(value="tid",required=false)Integer topicId,
    		@RequestParam(value="lt",required=false)Integer listType,
    		@RequestParam(value="jid")Integer jokeId,
    		HttpServletRequest request){
    	String uid = CookieUtil.getCookie(request);
    	impr.info(new ImprLog(distributorId, channelId, uid, Constants.IMPR_LOG_TYPE_DETAIL).toString());
    	clk.info(new ClickLog(jokeId, uid, Constants.CLK_LOG_TYPE_DETAIL).toString());
        return new Success(jokeService.getJoke(distributorId, channelId,topicId, listType,jokeId));
    }

    /**
     * 列表页图片展开日志
     * @param distributorId 渠道编号
     * @param channelId     频道编号(目前只支持推荐频道、图片频道)
     * @param jokeId        段子编号
     * @param request
     * @return
     */
    @RequestMapping(value = "/imageOpenLog")
    @ResponseBody
    public Result imageOpenLog(@RequestParam(value="did")Integer distributorId,
                               @RequestParam(value="cid")Integer channelId,
                               @RequestParam(value="jid",required = false)Integer jokeId,
                       HttpServletRequest request){
        String uid = CookieUtil.getCookie(request);
        impr.info(new ImprLog(distributorId, channelId, uid, Constants.IMPR_LOG_TYPE_DETAIL).toString());
        iol.info(new ImprLog(distributorId, channelId, uid, Constants.IMPR_LOG_TYPE_LIST, null).toString());
        return new Success();
    }

    /**
     * 专题列表
     * @param distributorId 渠道编号
     * @param channelId     频道编号
     * @param actionType    动作类型 (0:首次请求; 1:上拉刷新; 2:下拉刷新)
     * @param utc           UTC时间
     * @param count         返回记录数量
     * @param start
     * @param end
     * @param request
     * @return
     */
    @RequestMapping(value = "topicList")
    @ResponseBody
    public Result topicList(@RequestParam(value="did")Integer distributorId,
                            @RequestParam(value="cid")Integer channelId,
                            @RequestParam(value="at")Integer actionType,
                            @RequestParam(value="utc")Long utc,
                            @RequestParam(value="count")Integer count,
                            @RequestParam(value="start",required=false,defaultValue="0")Long start,
                            @RequestParam(value="end",required=false,defaultValue="9")Long end,
                        HttpServletRequest request){
        String uid = CookieUtil.getCookie(request);
        int type = Constants.IMPR_LOG_TYPE_LIST; // 列表页查询
        if(actionType == 0){
            type = Constants.IMPR_LOG_TYPE_CHANNEL; // 频道入口
        }
//        记录访问日志
        impr.info(new ImprLog(distributorId, channelId, uid, type).toString());
//        下拉刷新日志
        if(actionType == 2){
            dfl.info(new ImprLog(distributorId, channelId, uid, type, null).toString());
        }
//        上拉刷新日志
        if(actionType == 1){
            ufl.info(new ImprLog(distributorId, channelId, uid, type, null).toString());
        }
        return new Success(jokeService.topicList(actionType, utc, count, start, end));
    }

    /**
     * 专题详情
     * @param distributorId
     * @param channelId
     * @param topicId
     * @param start
     * @param end
     * @param request
     * @return
     */
    @RequestMapping(value = "topicDetails")
    @ResponseBody
    public Result topicDetails(@RequestParam(value="did")Integer distributorId,
                               @RequestParam(value="cid")Integer channelId,
                               @RequestParam(value="tid")Integer topicId,
                               @RequestParam(value="start",required=false,defaultValue="0")Long start,
                               @RequestParam(value="end",required=false,defaultValue="9")Long end,
                            HttpServletRequest request){
        String uid = CookieUtil.getCookie(request);
//        记录访问日志
        impr.info(new ImprLog(distributorId, channelId, uid, Constants.IMPR_LOG_TYPE_LIST).toString());
        return new Success(jokeService.topicDetails(topicId));
    }

}
