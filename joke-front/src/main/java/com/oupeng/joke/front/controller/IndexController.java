package com.oupeng.joke.front.controller;


import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.front.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
     * 推荐接口
     *
     * @param did  渠道id
     * @return
     */
    @RequestMapping(value = "/joke/index.html")
    public String getRecommendList(@RequestParam(value = "did", required = false, defaultValue = "0") String did, Model model) {
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
    @RequestMapping(value = "/joke/list")
    @ResponseBody
    public Result list(@RequestParam(value = "did", required = false, defaultValue = "0") Integer did,
                       @RequestParam(value = "cid", required = false, defaultValue = "1") Integer cid,
                       @RequestParam(value = "page", required = false, defaultValue = "10") Integer page,
                       @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        if(log.isDebugEnabled()){
            log.debug("收到来自渠道[{}]的请求!", did);
        }
        List<Joke> list = indexService.list(did, cid, page, limit);
        if(CollectionUtils.isEmpty(list)){
            return new Failed("获取失败!");
        }
        return new Success(list);
    }

}