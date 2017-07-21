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
     *
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

    /**
     * 批量插入
     *
     * @param list
     */
    @InsertProvider(method = "insertBatchComment", type = CommentSqlProvider.class)
    void insertBatchComment(@Param("list") List<Comment> list);

    @Delete(value = "delete from comment where uid=#{id}")
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


    /**
     * (缓存段子时进行，此时仅存在神评论)根据jokeId(sid)获取段子的神评
     *
     * @param sid 关联joke表中唯一id
     */
    @Select(value = " select id, content as bc, avata, nickname as nick,sid as jokeId, createtime as time, good from `comment` as c where sid = #{sid}")
    List<Comment> getGodReviewList(@Param("sid") Integer sid);


    /**
     * (缓存段子时进行，此时仅存在神评论)更新神评论的时间
     *
     * @param id            神评论的id
     * @param updateTime    更新时间
     * @param publish_state 1：已发布
     */
    @Update("update `comment` set  updatetime = #{updateTime}, publish_state = #{publish_state} where id = #{id}")
    void updateHotComment(@Param("id") Integer id, @Param("updateTime") Integer updateTime, @Param("publish_state") Integer publish_state);


    /**
     * 根据id获取Comment
     */
    @Select(value = "select id,good, sid as jokeId, content as bc, avata, nickname as nick from comment where id = #{id}")
    Comment getCommentById(@Param("id") Integer id);


    /**
     * 段子所有评论中 评论点赞数最大 评论信息
     */
    @Select("select id,good, sid as jokeId, content as bc, avata, nickname as nick from comment where sid = #{sid} order by good desc limit 1")
    Comment getMaxGoodCommentByJokeId(@Param("sid") Integer sid);

    /**
     * 根据sid获取Comment的id
     */
    @Select(value = "select id from comment where sid = #{sid}")
    List<Integer> getCommentId(@Param("sid") Integer sid);

}
