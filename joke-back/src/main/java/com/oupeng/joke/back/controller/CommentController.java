package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.CommentService;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.SBMapper;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.domain.user.SB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.List;

/**
 * Created by java_zong on 2017/4/18.
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController{
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;
    @Autowired
    private SBMapper sbmapper;
    @Autowired
    private JedisCache jedisCache;

    /**
     * 评论列表
     *
     * @param keyWord
     * @param state
     * @param pageNumber
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "keyWord", required = false) String keyWord,
                       @RequestParam(value = "state", required = false) Integer state,
                       @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                       Model model) {
        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        int pageCount = 0;//总页数
        int offset = 0;//开始条数index
        List<Comment> list = null;
        //	获取总条数
        int count = commentService.getListForVerifyCount(keyWord, state);
        if (count > 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }
            if (pageNumber > pageCount) {
                pageNumber = pageCount;
            }
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            offset = (pageNumber - 1) * pageSize;
            list = commentService.getListForVerify(keyWord, state, offset, pageSize);
        }
        model.addAttribute("list", list);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("state", state);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("count", count);

        return "/comment/list";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public Result verify(@RequestParam(value = "ids") String ids,
                         @RequestParam(value = "state") Integer state,
                         @RequestParam(value = "allState") Integer allState) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        SB sb2=new SB();
        sb2.setId(ids);

        sb2.setUpdateTime(Calendar.getInstance().getTime());
        sbmapper.insertASB(sb2);

        jedisCache.hset(JedisKey.BLACK_MAN,ids,ids);

        commentService.verifyComment(ids, state, allState, username);
        return new Success();
    }
}
