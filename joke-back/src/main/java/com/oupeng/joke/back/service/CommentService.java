package com.oupeng.joke.back.service;

import com.oupeng.joke.dao.mapper.CommentMapper;
import com.oupeng.joke.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论
 * Created by java_zong on 2017/4/17.
 */
@Component
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> getCommentForPublish() {
      return commentMapper.getCommentForPublish();
    }

    public void updateCommentState(String ids, Integer state) {
        commentMapper.updateCommentState(ids,state);
    }
}
