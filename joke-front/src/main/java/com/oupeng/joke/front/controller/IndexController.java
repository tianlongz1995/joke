package com.oupeng.joke.front.controller;


import com.oupeng.joke.domain.JokeDetail;
import com.oupeng.joke.domain.Relate;
import com.oupeng.joke.domain.Result;
import com.oupeng.joke.front.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    /**
     * 上拉刷新日志
     */
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IndexService indexService;

    /**
     * 首页
     *
     * @param did  渠道id
     * @return
     */
    @RequestMapping(value = "/joke/index.html")
    public String index(@RequestParam(value = "did", required = false, defaultValue = "2") String did, Model model) {
        if(log.isDebugEnabled()){
            log.debug("收到来自渠道[{}]的请求!", did);
        }
        indexService.getIndexConfig(did, model);
        return "index";
    }

    /**
     * 列表页
     * @param did
     * @param cid   1:趣图、2:段子、3:推荐、4:精选
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/joke2/list")
    @ResponseBody
    public Result list(@RequestParam(value = "did", required = false, defaultValue = "2") Integer did,
                       @RequestParam(value = "cid", required = false, defaultValue = "1") Integer cid,
                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit) {
        if(log.isDebugEnabled()){
            log.debug("收到来自渠道[{}]的请求!", did);
        }
        return indexService.list(did, cid, page, limit);
    }

    /**
     * 获取段子详情页
     * @param did
     * @param cid
     * @param jid
     * @return
     */
    @RequestMapping(value = "/joke2/details")
    @ResponseBody
    public Result details(@RequestParam(value = "did", required = false, defaultValue = "2") Integer did,
                          @RequestParam(value = "cid", required = false, defaultValue = "1") Integer cid,
                          @RequestParam(value = "jid", required = false, defaultValue = "10") Integer jid) {
        if(log.isDebugEnabled()){
            log.debug("收到来自渠道[{}]-[{}]-[{}]的请求!", did, cid, jid);
        }
        JokeDetail detail = indexService.getJokeDetail(did, cid, jid);
        if(detail == null){
            return new Result("获取失败!", 1);
        }
        return new Result(detail);
    }

    /**
     * 段子推荐
     * @param did
     * @param cid
     * @return
     */
    @RequestMapping(value = "/joke2/relate")
    @ResponseBody
    public Result relate(@RequestParam(value = "did", required = false, defaultValue = "2") Integer did,
                         @RequestParam(value = "cid", required = false, defaultValue = "1") Integer cid,
                         @RequestParam(value = "jid", required = false) Integer jid) {
        if(log.isDebugEnabled()){
            log.debug("收到来自渠道[{}]-[{}]的请求!", did, cid);
        }
        List<Relate> relates = indexService.getJokeRelate(did, cid,jid);
        if(relates == null){
            return new Result("获取失败!", 1);
        }
        return new Result(relates);
    }
    /**
     * 获取banner列表
     * @param cid 1 段子 2趣图 3推荐 4 精选
     * @return
     */
    @RequestMapping(value = "/joke2/banner")
    @ResponseBody
    public Result getBannerList(@RequestParam(value = "did") Integer did,
                                @RequestParam(value = "cid") Integer cid){

        return indexService.getBannerList(did,cid);
    }

}