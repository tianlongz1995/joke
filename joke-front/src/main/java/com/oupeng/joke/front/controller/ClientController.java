package com.oupeng.joke.front.controller;


import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.domain.EzineJoke;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.log.ClientLog;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.front.service.ClientService;
import com.oupeng.joke.front.util.Constants;
import com.oupeng.joke.front.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

    /**
     * 访问日志
     */
    private static final Logger client = LoggerFactory.getLogger("client");
//    /**
//     * 详情点击日志
//     */
//    private static final Logger clk = LoggerFactory.getLogger("clk");
//    /**
//     * 下拉刷新日志
//     */
//    private static final Logger dfl = LoggerFactory.getLogger("dfl");
//    /**
//     * 上拉刷新日志
//     */
//    private static final Logger ufl = LoggerFactory.getLogger("ufl");

    @Autowired
    private ClientService clientService;

    /**
     * 推荐接口
     *
     * @param did  渠道id
     * @param uid  用户id
     * @param at   0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort 排序值，记录上拉请求，用户看到的段子编号
     * @return
     */
    @RequestMapping(value = "recommend")
    @ResponseBody
    public Result getRecommendList(@RequestParam(value = "did") Integer did,
                                   @RequestParam(value = "uid") String uid,
                                   @RequestParam(value = "at") Integer at,
                                   @RequestParam(value = "sort", required = false, defaultValue = "0") Integer sort) {
        addLog(did, 20, uid, at, sort);
        List<Joke> recommendList = clientService.getRecommendList(uid, at, sort);
        return new Success(recommendList);
    }

//    /**
//     * 文字接口
//     *
//     * @param did  渠道id
//     * @param uid  用户id
//     * @param at   0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort 排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    @RequestMapping(value = "text")
//    @ResponseBody
//    public Result getTextList(@RequestParam(value = "did") Integer did,
//                              @RequestParam(value = "uid") String uid,
//                              @RequestParam(value = "at") Integer at,
//                              @RequestParam(value = "sort", required = false, defaultValue = "0") Integer sort) {
//        addLog(did, 14, uid, at, sort, 0);
//        List<Joke> textList = clientService.getTextList(14, uid, at, sort);
//        return new Success(textList);
//    }

//    /**
//     * 动图接口
//     *
//     * @param did  渠道id
//     * @param uid  用户id
//     * @param at   0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort 排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    @RequestMapping(value = "gift")
//    @ResponseBody
//    public Result getGiftList(@RequestParam(value = "did") Integer did,
//                              @RequestParam(value = "uid") String uid,
//                              @RequestParam(value = "at") Integer at,
//                              @RequestParam(value = "sort", required = false, defaultValue = "0") Integer sort) {
//        addLog(did, 16, uid, at, sort, 1);
//        List<Joke> giftList = clientService.getGiftList(16, uid, at, sort);
//        return new Success(giftList);
//    }

//    /**
//     * 图片接口
//     *
//     * @param did  渠道id
//     * @param uid  用户id
//     * @param at   0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort 排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    @RequestMapping(value = "image")
//    @ResponseBody
//    public Result getImageList(@RequestParam(value = "did") Integer did,
//                               @RequestParam(value = "uid") String uid,
//                               @RequestParam(value = "at") Integer at,
//                               @RequestParam(value = "sort", required = false, defaultValue = "0") Integer sort) {
//        addLog(did, 18, uid, at, sort, 2);
//        List<Joke> imageList = clientService.getImageList(18, uid, at, sort);
//        return new Success(imageList);
//    }

//    /**
//     * 专题接口
//     *
//     * @param did  渠道id
//     * @param uid  用户id
//     * @param at   0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort 排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    @RequestMapping(value = "topic")
//    @ResponseBody
//    public Result getTopicList(@RequestParam(value = "did") Integer did,
//                               @RequestParam(value = "uid") String uid,
//                               @RequestParam(value = "at") Integer at,
//                               @RequestParam(value = "sort", required = false) Integer sort) {
////        addLog(did, 22, uid, at, sort, 3);
////        List<Topic> topicList = clientService.getTopicList(uid, at, sort);
//        List<Topic> topicList = new ArrayList<>();
//        return new Success(topicList);
//    }

//    /**
//     * 专题详情接口
//     *
//     * @param did 渠道id
//     * @param uid 用户id
//     * @param tid 专题id
//     * @return
//     */
//    @RequestMapping(value = "topicDetail")
//    @ResponseBody
//    public Result geTopicDetailList(@RequestParam(value = "did") Integer did,
//                                    @RequestParam(value = "uid") String uid,
//                                    @RequestParam(value = "tid") Integer tid) {
//        client.info(new ClientLog("visit", did, 22, uid, Constants.IMPR_LOG_TYPE_LIST).toString());
//        List<Joke> topicDetailList = clientService.getTopicDetailList(tid);
//        return new Success(topicDetailList);
//    }

    /**
     * 踩接口
     *
     * @param id joke id
     * @return
     */
    @RequestMapping(value = "click")
    @ResponseBody
    public Result addClick(@RequestParam(value = "id") Integer id,
                           HttpServletRequest request) {
        String uid = CookieUtil.getCookie(request);
        client.info(new ClientLog("click", 22, null, uid, 1).toString());
        clientService.addClick(id);
        return new Success();
    }

    /**
     * 赞接口
     *
     * @param id joke id
     * @return
     */
    @RequestMapping(value = "like")
    @ResponseBody
    public Result addLike(@RequestParam(value = "id") Integer id,
                          HttpServletRequest request) {
        String uid = CookieUtil.getCookie(request);
        client.info(new ClientLog("click", 22, null, uid, 2).toString());
        clientService.addLike(id);
        return new Success();
    }

    /**
     * 记录日志
     *
     * @param did         渠道编号
     * @param cid         频道编号
     * @param uid         用户编号
     * @param at          0 首次访问,1 上拉,2 下拉
     * @param sort        上拉请求编号
     */
    public void addLog(int did, int cid, String uid, int at, int sort) {
//        int type = Constants.IMPR_LOG_TYPE_CHANNEL; //pv类型：频道入口
//        boolean flag = clientService.haveDropedDown(uid);
//        //下拉和上拉 首次请求
//        if (at == 1 && sort == 0 || !flag) {
//            type = Constants.IMPR_LOG_TYPE_CHANNEL; //pv类型 频道入口
//        }
        client.info(new ClientLog("visit", did, cid, uid, Constants.IMPR_LOG_TYPE_CHANNEL).toString());
        if (at == 2) {
            client.info(new ClientLog("dfl",did, cid, uid, Constants.IMPR_LOG_TYPE_CHANNEL, null).toString());
        }
        if (at == 1) {
            client.info(new ClientLog("ufl",did, cid, uid, Constants.IMPR_LOG_TYPE_CHANNEL, null).toString());
        }
    }

    /**
     * 开心一刻 - 欧朋浏览器导航笑话
     * @return
     */
    @RequestMapping(value = "ezineJoke")
    @ResponseBody
    public Result ezineJoke() {
        List<EzineJoke> list = clientService.ezineJoke();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",list);
        return new Success(jsonObject,3622417,219,"导航笑话");
    }
}