package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by java_zong on 2017/4/17.
 */
public interface CommentMapper {
    /**
     * 获取已审核待发布的评论列表
     *
     * @return
     */
    @Select("select id, content as bc, avata, nickname as nick,sid as jokeId, createtime as time, good from `comment` as c where c.state = 1 and c.publish_state = 0 ")
    List<Comment> getCommentForPublish();

    /**
     * 更新评论状态
     *
     * @param ids
     * @param state
     */
    @Update("update `comment` set publish_state = #{state} where id in (${ids})")
    void updateCommentState(@Param("ids") String ids, @Param("state") Integer state);


    /**
     * 更新点赞数
     *
     * @param id
     * @param good
     */
    @Update("update `comment` set good = good + #{good} where id = #{id}")
    void updateCommentGood(@Param("id") Integer id, @Param("good") Integer good);

    @Insert("insert into comment (`state`,`sid`,`uid`,`nickname`,`content`,`avata`,`good`,`createtime`,`publish_state`) values ('1',#{jokeId},#{uid},#{nick},#{bc},#{avata},#{createTime},'1')")
    @SelectKey(statement = "SELECT LAST_INSERT_ID() as id", keyProperty = "id", before = false, resultType = Integer.class)
    void insertComment(Comment com);
}
