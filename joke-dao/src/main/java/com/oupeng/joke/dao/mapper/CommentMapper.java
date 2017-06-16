package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.CommentSqlProvider;
import com.oupeng.joke.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by java_zong on 2017/4/17.
 */
public interface CommentMapper {
    /**
     * 获取已审核待发布的评论列表
     * @param ids
     * @param state
     * @param pubState
     * @return
     */
    @SelectProvider(method = "getCommentForPublish", type = CommentSqlProvider.class)
    List<Comment> getCommentForPublish(@Param("ids") String ids, @Param("state") Integer state, @Param("pubState") Integer pubState);

    /**
     * 更新评论发布状态
     *
     * @param ids
     * @param state
     */
    @Update("update `comment` set publish_state = #{state} where id in (${ids})")
    void updateCommentPubState(@Param("ids") String ids, @Param("state") Integer state);

    /**
     * 更新评论状态
     *
     * @param ids
     * @param state
     * @param username
     */
    @Update("update `comment` set state = #{state}, adminid = #{username}, updatetime = #{updateTime} where id in (${ids})")
    void updateCommentState(@Param("ids") String ids, @Param("state") Integer state, @Param("username") String username, @Param("updateTime") Integer updateTime);

    /**
     * 更新点赞数
     *
     * @param id
     * @param good
     */
    @Update("update `comment` set good = good + #{good} where id = #{id}")
    void updateCommentGood(@Param("id") Integer id, @Param("good") Integer good);


    @Insert("insert into comment (`state`,`sid`,`uid`,`nickname`,`content`,`avata`,`good`,`createtime`,`publish_state`) values ('1',#{jokeId},#{uid},#{nick},#{bc},#{avata},#{good},#{time},'1')")
    @SelectKey(statement = "SELECT LAST_INSERT_ID() as id", keyProperty = "id", before = false, resultType = Integer.class)
    void insertComment(Comment com);

    @Delete(value="delete from comment where uid=#{id}")
    void deleteComment(@Param("id") String id);


            /**
             * 评论数据
             *
             * @param keyWord 关键字
             * @param state   状态
             * @return
             */
    @SelectProvider(method = "getListForVerifyCount", type = CommentSqlProvider.class)
    int getListForVerifyCount(@Param("keyWord") String keyWord, @Param("state") Integer state);


    @SelectProvider(method = "getListForVerify", type = CommentSqlProvider.class)
    List<Comment> getListForVerify(@Param("keyWord") String keyWord, @Param("state") Integer state, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}
