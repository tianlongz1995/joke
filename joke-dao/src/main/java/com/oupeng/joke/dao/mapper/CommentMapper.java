package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.domain.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by java_zong on 2017/4/17.
 */
public interface CommentMapper {
    /**
     * 获取已审核待发布的评论列表
     * @return
     */
    @Select("select id, content as bc, avata, nickname as nick,sid as jokeId, createtime as time, good from `comment` as c where c.state = 1 and c.publish_state = 0 ")
    List<Comment> getCommentForPublish();

    /**
     * 更新评论状态
     * @param ids
     * @param state
     */
    @Update("update `comment` set publish_state = #{state} where id in (${ids})")
    void updateCommentState(@Param("ids") String ids, @Param("state") Integer state);

    /**
     * 评论点赞
     * @param id
     */
    @Update("update `comment` set good = good+1 where id = #{id}")
    void likeComment(Integer id);
}
