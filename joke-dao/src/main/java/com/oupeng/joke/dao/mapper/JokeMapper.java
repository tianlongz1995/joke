package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.JokeSqlProvider;
import com.oupeng.joke.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface JokeMapper {
    /**
     * 获取段子列表总数
     *
     * @param type
     * @param status
     * @return
     */
    @SelectProvider(method = "getJokeListForTopicCount", type = JokeSqlProvider.class)
    int getJokeListForTopicCount(@Param("type") Integer type, @Param("status") Integer status);

    /**
     * 获取专题段子列表
     *
     * @param type
     * @param status
     * @param offset
     * @param pageSize
     * @return
     */
    @SelectProvider(method = "getJokeListForTopic", type = JokeSqlProvider.class)
    List<Joke> getJokeListForTopic(@Param("type") Integer type, @Param("status") Integer status, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    /**
     * 获取数据源列表
     *
     * @param type
     * @param status
     * @param id
     * @param content
     * @param isTopic
     * @return
     */
    @SelectProvider(method = "getJokeList", type = JokeSqlProvider.class)
    List<Joke> getJokeList(@Param(value = "type") Integer type, @Param(value = "status") Integer status,
                           @Param(value = "id") Integer id, @Param(value = "content") String content, @Param(value = "isTopic") boolean isTopic);

    /**
     * 获取段子信息
     *
     * @param id
     * @return
     */
    @Select(value = "select id,title,content,img,gif,type,status,source_id as sourceId,verify_user as verifyUser,verify_time as verifyTime,"
            + "create_time as createTime,update_time as updateTime,good,bad,width,height,weight,comment_number as commentNumber, comment as commentContent, avata, nick, src from joke where id = ${id}")
    @ResultType(value = Joke.class)
    Joke getJokeById(@Param(value = "id") Integer id);

    /**
     * 修改段子信息
     *
     * @param joke
     * @return
     */
    @UpdateProvider(method = "updateJoke", type = JokeSqlProvider.class)
    int updateJoke(Joke joke);


    /**
     * 统计已审核的段子数量(不包含置顶段子)
     * @param user
     * @return
     */
    @Select(value = "select type,count(1) as num from joke where "// DATE_FORMAT(verify_time,'%y-%m-%d') = CURDATE() and"
            + "status = 1 and audit != 6 "
            //+ "and verify_user =#{user}"
            +" group by type ")
    List<JokeVerifyInfo> getJokeVerifyInfoByUser(@Param(value = "user") String user);

    /**
     * 更新段子被踩数
     *
     * @param id
     */
    @Update(value = "update joke set bad = bad + 1 where id = #{id}")
    void updateJokeStepCount(@Param(value = "id") Integer id);

    /**
     * 更新精选的被点踩数
     *
     * @param id
     */
    @Update(value = "update choice set bad = bad + 1 where id = #{id}")
    void updateChoiceStepCount(@Param(value = "id") Integer id);

    /**
     * 更新段子点赞数
     *
     * @param id
     */
    @Update(value = "update joke set good = good + 1 where id = #{id}")
    void updatejokeLikeCount(@Param(value = "id") Integer id);

    /**
     * 更新精选点赞数
     *
     * @param id
     */
    @Update(value = "update choice set good = good + 1 where id = #{id}")
    void updateChoiceLikeCount(@Param(value = "id") Integer id);

    /**
     * 保存反馈信息
     *
     * @param feedback
     */
    @Insert("INSERT INTO feedback (d_id, c_id, type, content, create_time) VALUES (#{distributorId}, #{channelId}, #{type}, #{content}, now())")
    void insertJokeFeedback(Feedback feedback);

    @SelectProvider(method = "getJokeListForChannel", type = JokeSqlProvider.class)
    List<Joke> getJokeListForChannel(@Param(value = "contentType") String contentType, @Param(value = "start") Integer start,
                                     @Param(value = "size") Integer size);

    @SelectProvider(method = "getJokeCountForChannel", type = JokeSqlProvider.class)
    int getJokeCountForChannel(String contentType);

    /**
     * 获取专题对应的段子编号
     *
     * @param topicId
     * @return
     */
    @Select(value = "select j_id from topic_joke where `status` = 0 and t_id = #{topicId}")
    List<Integer> getJokeForPublishTopic(@Param("topicId") Integer topicId);

    /**
     * 查询最近审核通过的数据
     *
     * @param contentType
     * @param size        发布数量
     * @return
     */
    @SelectProvider(method = "getJokeForPublishChannel", type = JokeSqlProvider.class)
    List<Joke> getJokeForPublishChannel(@Param(value = "contentType") String contentType,
                                        @Param(value = "size") Integer size);

    @Select(value = "select count(1) from joke t where t.`status` = #{status} and "
            + "t.type in (${contentType}) and not EXISTS ( select 1 from topic_joke where j_id = t.id) ")
    int getJokeCountForPublishChannel(@Param(value = "contentType") String contentType, @Param(value = "status") Integer status);

    /**
     * 获取待发布的段子列表
     *
     * @param lastUpdateTime
     * @param currentUpdateTime
     * @return
     */
    @SelectProvider(method = "getJokeListForPublish", type = JokeSqlProvider.class)
    List<Joke> getJokeListForPublish(@Param(value = "lut") String lastUpdateTime, @Param(value = "cut") String currentUpdateTime);

    /**
     * 查询推荐频道下数据内容
     * <pre>
     *     图片55条，GIF15条，文字30条
     * </pre>
     *
     * @return
     */
    @SelectProvider(method = "getJokeListForPublishRecommend", type = JokeSqlProvider.class)
    List<String> getJokeListForPublishRecommend(@Param(value = "type") Integer type,
                                                @Param(value = "num") Integer num);

    @Select(value = " select t1.source_id as soureId,t1.validNum as validNum,case when t2.inValidNum is null then 0 else t2.inValidNum end as inValidNum from "
            + " (select source_id,count(1) as validNum,0 as inValidNum from joke where `status` = 1 and  DATE_FORMAT(verify_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) GROUP BY source_id)t1 "
            + " LEFT JOIN"
            + " (select source_id,0 as validNum,count(1) as inValidNum from joke where `status` = 2 and  DATE_FORMAT(verify_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) GROUP BY source_id)t2 "
            + " on t1.source_id = t2.source_id "
            + " union "
            + " select t2.source_id as soureId,case when t1.validNum is null then 0 else t1.validNum end  as validNum,t2.inValidNum  as inValidNum from "
            + " (select source_id,count(1) as validNum,0 as  inValidNum from joke where `status` = 1 and  DATE_FORMAT(verify_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) GROUP BY source_id)t1 "
            + " RIGHT JOIN "
            + " (select source_id,0 as validNum,count(1) as  inValidNum from joke where `status` = 2 and  DATE_FORMAT(verify_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) GROUP BY source_id)t2 "
            + " on t1.source_id = t2.source_id ")
    List<JokeVerifyRate> getJokeVerifyRate();

    /**
     * 存储段子信息 - 并返回主键
     *
     * @param joke
     */
    @InsertProvider(method = "insertJoke", type = JokeSqlProvider.class)
    @SelectKey(statement = "SELECT LAST_INSERT_ID() as id", keyProperty = "id", before = false, resultType = Integer.class)
    void insertJoke(Joke joke);

    /**
     * 更新段子状态
     *
     * @param status
     * @param ids
     * @param user
     */
    @UpdateProvider(method = "updateJokeStatus", type = JokeSqlProvider.class)
    void updateJokeStatus(@Param(value = "status") Integer status,
                          @Param(value = "ids") String ids,
                          @Param(value = "user") String user);

    /**
     * 更新段子置顶状态 - 更新置顶状态不改变更新时间, 避免影响段子、趣图发布时的顺序
     *
     * @param ids
     * @param user
     */
    @Update("update joke set audit = 6, verify_time = now(), verify_user= #{user} where id in (${ids})")
    void updateJokeTopAudit(@Param("ids") String ids, @Param("user") String user);

    /**
     * 更新段子状态为已通过、置顶, 并修改更新时间
     *
     * @param ids
     * @param user
     */
    @Update("update joke set status = 1 ,audit = 6, verify_time = now(), verify_user= #{user}, update_time = now() where id in (${ids})")
    void updateJokeToTopAndAudit(@Param("ids") String ids, @Param("user") String user);

    /**
     * 获取字典记录总条数
     *
     * @param code
     * @return
     */
    @Select(value = "select count(1) from dictionary where `parent_code` = #{code}")
    int getDictionaryRecordCount(@Param(value = "code") String code);

    /**
     * 获取字典记录列表
     *
     * @param code
     * @param offset
     * @param pageSize
     * @return
     */
    @Select(value = "select id, code, parent_code as parentCode, type, value, `describe`, `seq`,update_time as updateTime from dictionary where parent_code = #{code} order by seq asc limit #{offset}, #{pageSize} ")
    List<Dictionary> getDictionaryRecordList(@Param(value = "code") String code, @Param(value = "offset") int offset, @Param(value = "pageSize") Integer pageSize);

    /**
     * 添加权重字典
     *
     * @param dict
     * @return
     */
    @InsertProvider(method = "addDictionary", type = JokeSqlProvider.class)
    int addDictionary(Dictionary dict);

    /**
     * 修改权重信息
     *
     * @param dict
     * @return
     */
    @UpdateProvider(method = "weightEdit", type = JokeSqlProvider.class)
    int weightEdit(Dictionary dict);

    /**
     * 删除权限信息
     *
     * @param id
     * @return
     */
    @Delete("delete from dictionary where id = #{id}")
    int weightDel(@Param("id") Integer id);

    /**
     * 获取字典信息
     *
     * @param id
     * @return
     */
    @Select("select id, `code`, parent_code as parentCode, `value`, `describe`, `seq` from dictionary where id= #{id}")
    Dictionary weightGet(@Param("id") String id);

    /**
     * 获取待审核的段子列表
     *
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @return
     */
    @SelectProvider(method = "getJokeListForVerify", type = JokeSqlProvider.class)
    List<Joke> getJokeListForVerify(@Param("type") Integer type, @Param("status") Integer status, @Param("source") Integer source, @Param("startDay") String startDay, @Param("endDay") String endDay, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    /**
     * 获取待审核的段子列表记录总数
     *
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @return
     */
    @SelectProvider(method = "getJokeListForVerifyCount", type = JokeSqlProvider.class)
    int getJokeListForVerifyCount(@Param("type") Integer type, @Param("status") Integer status, @Param("source") Integer source, @Param("startDay") String startDay, @Param("endDay") String endDay);

    /**
     * 修改发布数量
     *
     * @param id
     * @param size
     * @return
     */
    @Update("update channel set size = #{size} where id = #{id}")
    int editPublishSize(@Param("id") String id, @Param("size") Integer size);

    /**
     * 查询段子列表
     *
     * @param type  段子类型: 0:文字、1:图片、2:动图
     * @param count 获取段子数量
     * @return
     */
    @SelectProvider(method = "getPublishJokeListByType", type = JokeSqlProvider.class)
    List<Joke> getPublishJokeListByType(@Param("type") Integer type, @Param("count") Integer count);

    /**
     * 自动发布段子
     *
     * @param type
     * @param limit
     */
    @Update("update joke set update_time =now(),status=1,verify_time=now(),verify_user='admin' where id in (select t.id from (select id from joke where type=#{type} and status = 0 order by create_time limit #{limit}) as t)")
    void autoAuditJoke(@Param("type") int type, @Param("limit") int limit);

    /**
     * 添加发布规则
     *
     * @param code
     * @param value
     */
    @UpdateProvider(method = "addPublishRole", type = JokeSqlProvider.class)
    void addPublishRole(@Param(value = "code") Integer code,
                        @Param(value = "value") String value);

    /**
     * 获取发布规则
     *
     * @param code
     */
    @Select(value = "select value from dictionary where code = #{code}")
    String getPublishRole(Integer code);

    /**
     * 增加神评数量
     *
     * @param jid
     */
    @Update("update joke set comment_number = comment_number + 1 where id = #{jid}")
    void incrementComment(@Param("jid") Integer jid);

    /**
     * 增加精选的评论数
     *
     * @param jid
     */
    @Update("update choice set comment_number = comment_number + 1 where id = #{jid}")
    void incrementChoiceComment(@Param(value = "jid") Integer jid);

    /**
     * 减少神评数量
     *
     * @param jid
     */
    @Update("update joke set comment_number = comment_number - 1 where id = #{jid}")
    void decrementComment(@Param("jid") Integer jid);


    /**
     * 减少神评数量
     *
     * @param jid
     */
    @Update("update choice set comment_number = comment_number - 1 where id = #{jid}")
    void decrementChoiceComment(@Param("jid") Integer jid);

    /**
     * 获取段子2.0文字、趣图段子发布列表
     *
     * @param type
     * @param limit
     * @return
     */
    @Select("select id,weight from joke where status = #{status} and type = #{type} order by update_time desc limit #{limit}")
    List<Joke> getJoke2PublishList(@Param("status") int status, @Param("type") int type, @Param("limit") int limit);

    /**
     * 获取段子2.0推荐段子发布列表
     *
     * @param type
     * @param limit
     * @return
     */
    @Select("select id from joke where audit = 1 and status = #{status} and type = #{type} order by update_time desc limit #{limit}")
    List<Joke> getJoke2RecommendPublishList(@Param("status") int status, @Param("type") int type, @Param("limit") int limit);

    /**
     * 更新段子2.0文字段子已发布状态
     *
     * @param idsStr
     */
    @Update("update joke set update_time = now(), status = #{status} where id in (${idsStr})")
    void updateJoke2PublishStatus(@Param("idsStr") String idsStr, @Param("status") Integer status);

    /**
     * 更新段子2.0文字段子已推荐状态
     *
     * @param idsStr
     */
    @Update("update joke set update_time = now(), status = 4, audit = 4 where id in (${idsStr})")
    void updateJoke2RecommendPublishStatus(@Param("idsStr") String idsStr);

    /**
     * 获取段子2.0发布任务
     *
     * @return
     */
    @Select("select id, `describe` as name, `type`, `value` as policy from dictionary where code in('10041','10042','10043')")
    List<Task> getJoke2PublishTask();

    /**
     * 保存置顶段子
     *
     * @param ids
     */
    @InsertProvider(method = "insertJokeTop", type = JokeSqlProvider.class)
    void insertJokeTop(@Param("ids") String ids);

    /**
     * 获取当前时间发布的段子列表
     *
     * @param preReleaseHour
     * @param releaseTime
     * @return
     */
    @Select("select t.jid as id, j.type, t.sort from joke_top t left join joke j on t.jid = j.id where t.release_time <= #{releaseTime} and t.release_time >= #{preReleaseHour} and t.status = 1 and j.audit = 6 order by t.sort asc")
    List<Joke> getJoke2RecommendTopList(@Param("preReleaseHour") String preReleaseHour, @Param("releaseTime") String releaseTime);

    /**
     * 更新首页置顶段子状态
     *
     * @param idsStr
     */
    @Update("update joke_top set update_time = now(), status = 2, update_user= 'systemTask' where jid in (${idsStr}) ")
    void updateJokeTopPublishStatus(@Param("idsStr") String idsStr);

    /**
     * 获取首页置顶段子总数
     *
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @return
     */
    @SelectProvider(method = "getJokeTopListCount", type = JokeSqlProvider.class)
    int getJokeTopListCount(@Param("type") Integer type,
                            @Param("status") Integer status,
                            @Param("source") Integer source,
                            @Param("startDay") String startDay,
                            @Param("endDay") String endDay);

    /**
     * 获取首页置顶段子列表
     *
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @param offset
     * @param pageSize
     * @return
     */
    @SelectProvider(method = "getJokeTopList", type = JokeSqlProvider.class)
    List<JokeTop> getJokeTopList(@Param("type") Integer type,
                                 @Param("status") Integer status,
                                 @Param("source") Integer source,
                                 @Param("startDay") String startDay,
                                 @Param("endDay") String endDay,
                                 @Param("offset") int offset,
                                 @Param("pageSize") Integer pageSize);

    /**
     * 发布首页置顶段子
     *
     * @param id
     * @param sort
     * @param releaseTime
     * @param username
     */
    @Update("update joke_top set update_time = now(), status = 1, sort = #{sort}, update_user= #{username}, release_time = #{releaseTime} where jid = #{id}")
    int releaseTopJoke(@Param("id") Integer id,
                       @Param("sort") Integer sort,
                       @Param("releaseTime") String releaseTime,
                       @Param("username") String username);

    /**
     * 修改排序值
     *
     * @param id
     * @param sort
     * @param username
     * @return
     */
    @Update("update joke_top set update_time = now(), update_user= #{username}, sort = #{sort} where jid = #{id}")
    int editTopJokeSort(@Param("id") Integer id,
                        @Param("sort") Integer sort,
                        @Param("username") String username);

    /**
     * 获取段子2.0记录数量
     *
     * @param type
     * @return
     */
    @Select("select count(id) from joke where type = #{type} and status = #{status}")
    int getJokeListForCount(@Param("type") Integer type, @Param("status") Integer status);

    /**
     * 获取待缓存的段子信息
     *
     * @param jokeIds
     * @return
     */
    @Select("select id,title,content,img,gif,type,good,bad,width,height,weight,comment_number as commentNumber, comment as commentContent, avata, nick, src, release_avata ra ,release_nick rn from joke where id in (${jokeIds})")
    List<Joke> getCacheJokeListByIds(@Param("jokeIds") String jokeIds);

    /**
     * 置顶段子下线
     *
     * @param ids
     * @param username
     * @return
     */
    @Update("update joke_top set update_time = now(), update_user= #{username}, status = 3 where jid in (${ids})")
    int topJokeOffline(@Param("ids") String ids, @Param("username") String username);

    /**
     * 新增段子
     *
     * @param joke
     * @return
     */
    @InsertProvider(method = "addJoke", type = JokeSqlProvider.class)
    @SelectKey(statement = "SELECT LAST_INSERT_ID() as id", keyProperty = "id", before = false, resultType = Integer.class)
    void addJoke(Joke joke);

    /**
     * 修改图片地址
     *
     * @param id
     * @param img
     */
    @Update("update joke set img = #{img} where id = #{id}")
    void editImgPath(@Param("id") Integer id, @Param("img") String img);

    /**
     * 获取置顶段子已审核统计
     *
     * @param user
     * @return
     */
    @Select("select j.type, count(j.id) as num from joke j right join joke_top jt on j.id=jt.jid where "
           // + "DATE_FORMAT(j.verify_time,'%y-%m-%d') = CURDATE() and "
            + " j.audit = 6 "
           //+ " and j.verify_user = #{user} "
            + " and jt.status = 0 group by j.type")
    List<JokeVerifyInfo> getJokeTopVerifyInfoByUser(@Param("user") String user);

    /**
     * 获取段子昵称
     *
     * @return
     */
    @Select("select nickname from nickname")
    List<String> getJokeNick();

    /**
     * 获取段子神评数量
     *
     * @param id
     * @return
     */
    @Select("select count(c.id) as total , sid as jokeId from `comment` c where c.good >= 10 and c.state = 1 and c.sid in (${id})  group by c.sid")
    List<Comment> getReplyNum(@Param("id") String id);

    /**
     * 神评中点赞数最大的一条神评，插入到joke中
     *
     * @param id
     * @param comment
     * @param avata
     * @param nick
     */
    @Update("update joke set comment = #{comment}, avata = #{avata}, nick = #{nick} where id = #{id}")
    void updateJokeOfGod(@Param("id") Integer id, @Param("comment") String comment, @Param("avata") String avata, @Param("nick") String nick);

    /**
     * 获取数据源source_id(在time时间之前)的爬取数据记录
     *
     * @param time
     * @param source_id
     * @return
     */
    @Select(" select id, status, source_id as sourceId, src, comment_number as commentNumber, comment as commentContent, avata, nick from joke where source_id = #{source_id} and create_time < #{time} and status = 0 and isrespider = 0 order by id limit 800")
    List<Joke> getJokebeforeTime(@Param("time") String time, @Param("source_id") Integer source_id);

    /**
     * 删除joke的评论
     */
    @Delete("delete from comment where sid = #{jokeId}")
    void deleteByJokeId(@Param("jokeId")Integer jokeId);

    /**
     * joke插入神评论信息
     */
    @Update("update joke set comment_number = #{godNum}, comment = #{comment}, avata = #{avata}, nick = #{nick}, isrespider = #{isrespider}  where id = #{id} ")
    void updateJokeComment(@Param("id") Integer id, @Param("godNum") Integer godNum, @Param("comment") String comment, @Param("avata") String avata, @Param("nick") String nick, @Param("isrespider") Integer isrespider);

}
