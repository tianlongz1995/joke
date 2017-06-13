package com.oupeng.joke.front.controller;

import com.google.common.collect.Maps;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.comment.Page;
import com.oupeng.joke.domain.comment.Result;

import com.oupeng.joke.front.service.CommentService;
import com.oupeng.joke.front.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * Created by java_zong on 2017/4/19.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 评论列表
     *
     * @param jid 段子id
     * @return
     */

    @RequestMapping(value = "/joke/getComment")
    @ResponseBody
    public Result getComment(@RequestParam(value = "jid") Integer jid,
                             @RequestParam(value = "current", required = false) Integer current) {
        int status = 0;
        //当前页数
        current = current == null ? 1 : current;
        //每页显示条数
        int pageSize = 10;
        int pageCount = 0;
        int offset = 0;
        int endset = 0;
        List<Comment> list = null;
        //获取总条数
        int count = commentService.getCommentCount(jid);
        if (count > 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }
            if (current > pageCount) {
                current = pageCount;
            }
            if (current < 1) {
                current = 1;
            }
            offset = (current - 1) * pageSize;
            endset = offset + pageSize - 1;
            list = commentService.getComment(jid, offset, endset, false);

        }
        if (CollectionUtils.isEmpty(list)) { //无评论
            status = 1;
            return new Result(status);
        }

        Map<String, Object> data = Maps.newHashMap();
        Page page = new Page(pageSize, current, pageCount);
        List<Comment> hotList = commentService.getComment(jid, null, null, true);
        data.put("total", count);
        data.put("hottest", hotList);
        data.put("newest", list);
        data.put("page", page);
        return new Result(status, data);
    }


    @RequestMapping(value = "/joke/likeComment")
    @ResponseBody
    public Result likeComment(@RequestParam(value = "id") Integer id) {
        commentService.likeComment(id);
        return new Result(0);

    }

    /**
     * 评论先发后审
     *
     * @param jid
     * @param comment
     * @return
     */
    @RequestMapping(value = "/joke/sendComment")
    @ResponseBody
    public Result sendComment(@RequestParam(value = "jid") Integer jid,
                              @RequestParam(value = "comment") String comment,
                              @RequestParam(value = "userId") Integer userId,
                              @RequestParam(value = "nick") String nick,
                              @RequestParam(value = "avata") String avata) {
        try {
            comment = new String(comment.getBytes("ISO-8859-1"), "utf-8");
            nick = new String(nick.getBytes("ISO-8859-1"), "utf-8");
            return commentService.sendComment(jid, comment, userId, nick, avata);
        } catch (Exception e) {
            return new Result(1);
        }

    }

    /**
     * 随机用户接口
     *
     * @return
     */
    @RequestMapping(value = "/joke/userDetail")
    @ResponseBody
    public Comment user() {
        Comment comment = HttpUtil.getRandomUser("http://joke2.oupeng.com/comment/joke/user");
        return comment;
    }

}