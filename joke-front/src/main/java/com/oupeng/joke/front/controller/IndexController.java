package com.oupeng.joke.front.controller;


import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.front.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    /**
     * 访问日志
     */
    private static final Logger impr = LoggerFactory.getLogger("impr");
    /**
     * 详情点击日志
     */
    private static final Logger clk = LoggerFactory.getLogger("clk");
    /**
     * 下拉刷新日志
     */
    private static final Logger dfl = LoggerFactory.getLogger("dfl");
    /**
     * 上拉刷新日志
     */
    private static final Logger ufl = LoggerFactory.getLogger("ufl");

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
    @RequestMapping(value = "/index.html")
    public String getRecommendList(@RequestParam(value = "did", required = false, defaultValue = "0") String did, Model model) {
        indexService.getIndexConfig(did, model);
        return "index";
    }


}