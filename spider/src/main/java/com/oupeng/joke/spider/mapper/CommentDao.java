package com.oupeng.joke.spider.mapper;

import com.oupeng.joke.spider.domain.Comment;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by zongchao on 2017/3/9.
 */
public interface CommentDao {

    @Insert("insert into comment (`state`,`sid`,`uid`,`nickname`,`content`,`avata`,`good`,`createtime`,`updatetime`)values('1',#{sid},#{uid},#{nickname},#{content},#{avata},#{good},#{createTime},#{createTime})")
    void addComment(Comment comment);
}
