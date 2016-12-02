package com.oupeng.joke.dao.mapper;

import java.util.List;



import com.oupeng.joke.dao.sqlprovider.JokeSqlProvider;
import com.oupeng.joke.domain.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Delete;

public interface JokeMapper {
	/**
	 * 获取段子列表总数
	 * @param type
	 * @param status
	 * @return
	 */
	@SelectProvider(method="getJokeListForTopicCount",type=JokeSqlProvider.class)
	int getJokeListForTopicCount(@Param("type")Integer type, @Param("status")Integer status);

	/**
	 * 获取专题段子列表
	 * @param type
	 * @param status
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	@SelectProvider(method="getJokeListForTopic",type=JokeSqlProvider.class)
	List<Joke> getJokeListForTopic(@Param("type")Integer type, @Param("status")Integer status, @Param("offset")Integer offset, @Param("pageSize")Integer pageSize);

	/**
	 * 获取数据源列表
	 * @param type
	 * @param status
	 * @param id
	 * @param content
	 * @param isTopic
	 * @return
	 */
	@SelectProvider(method="getJokeList",type=JokeSqlProvider.class)
	List<Joke> getJokeList(@Param(value="type")Integer type, @Param(value="status")Integer status,
						   @Param(value="id")Integer id, @Param(value="content")String content, @Param(value="isTopic")boolean isTopic);
	
	@Select(value="select id,title,content,img,gif,type,status,source_id as sourceId,verify_user as verifyUser,verify_time as verifyTime,"
			+ "create_time as createTime,update_time as updateTime,good,bad,width,height from joke where id = ${id}")
	@ResultType(value=Joke.class)
	Joke getJokeById(@Param(value="id")Integer id);
	
	@UpdateProvider(method="updateJoke",type=JokeSqlProvider.class)
	void updateJoke(Joke joke);
	
	@Select(value="select type,count(1) as num from joke where DATE_FORMAT(verify_time,'%y-%m-%d') = CURDATE() "
			+ " and status = 1 and verify_user =#{user} group by type ")
	List<JokeVerifyInfo> getJokeVerifyInfoByUser(@Param(value="user")String user);

	/**
	 * 更新段子被踩数
	 * @param id
	 */
	@Update(value="update joke set bad = bad + 1 where id = #{id}")
	void updateJokeStepCount(@Param(value = "id")Integer id);

	/**
	 * 更新段子点赞数
	 * @param id
	 */
	@Update(value="update joke set good = good + 1 where id = #{id}")
	void updatejokeLikeCount(@Param(value = "id")Integer id);

	/**
	 * 保存反馈信息
	 * @param feedback
	 */
	@Insert("INSERT INTO feedback (d_id, c_id, type, content, create_time) VALUES (#{distributorId}, #{channelId}, #{type}, #{content}, now())")
	void insertJokeFeedback(Feedback feedback);
	
	@SelectProvider(method="getJokeListForChannel",type=JokeSqlProvider.class)
	List<Joke> getJokeListForChannel(@Param(value="contentType")String contentType,@Param(value="start")Integer start,
			@Param(value="size")Integer size);
	
	@SelectProvider(method="getJokeCountForChannel",type=JokeSqlProvider.class)
	int getJokeCountForChannel(String contentType);

	/**
	 * 获取专题对应的段子编号
	 * @param topicId
	 * @return
	 */
	@Select(value="select j_id from topic_joke where `status` = 0 and t_id = #{topicId}")
	List<Integer> getJokeForPublishTopic(@Param("topicId")Integer topicId);

	/**
	 * 查询最近审核通过的数据
	 * @param contentType
	 * @param size		发布数量
	 * @return
	 */
	@Select(value="select t.id,t.verify_time as verifyTime from joke t where t.`status` = 1 and "
			+ "t.type in (${contentType}) and not EXISTS ( select 1 from topic_joke where j_id = t.id) order by t.verify_time desc limit #{size} ")
	List<Joke> getJokeForPublishChannel(@Param(value = "contentType") String contentType, @Param(value = "size")Integer size);
	
	@Select(value="select count(1) from joke t where t.`status` = #{status} and "
			+ "t.type in (${contentType}) and not EXISTS ( select 1 from topic_joke where j_id = t.id) ")
	int getJokeCountForPublishChannel(@Param(value="contentType")String contentType,@Param(value="status")Integer status);

	/**
	 * 获取待发布的段子列表
	 * @param lastUpdateTime
	 * @param currentUpdateTime
	 * @return
	 */
	@SelectProvider(method="getJokeListForPublish",type=JokeSqlProvider.class)
	List<Joke> getJokeListForPublish(@Param(value="lut")String lastUpdateTime,@Param(value="cut")String currentUpdateTime);

	/**
	 * 查询推荐频道下数据内容
	 * <pre>
	 *     图片55条，GIF15条，文字30条
	 * </pre>
	 * @return
	 */
	@Select(value = "select id FROM joke t where `status` = 3 and `type` = #{type} and DATE_FORMAT(update_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) and not EXISTS ( select 1 from topic_joke where j_id = t.id) ORDER BY good desc limit #{num} ")
	List<String> getJokeListForPublishRecommend(@Param(value="type")Integer type,@Param(value="num")Integer num);
	
	@Select(value=" select t1.source_id as soureId,t1.validNum as validNum,case when t2.inValidNum is null then 0 else t2.inValidNum end as inValidNum from "
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
	 * @param joke
	 */
	@InsertProvider(method="insertJoke",type=JokeSqlProvider.class)
	@SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=Integer.class)
	void insertJoke(Joke joke);

	/**
	 * 更新段子状态
	 * @param status
	 * @param ids
	 * @param user
	 */
	@UpdateProvider(method="updateJokeStatus",type=JokeSqlProvider.class)
	void updateJokeStatus(@Param(value="status")Integer status,@Param(value="ids")String ids,@Param(value="user")String user);

	/**
	 * 获取字典记录总条数
	 * @param code
	 * @return
	 */
	@Select(value="select count(1) from dictionary where `parent_code` = #{code}")
	int getDictionaryRecordCount(@Param(value="code")String code);

	/**
	 * 获取字典记录列表
	 * @param code
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	@Select(value="select id, code, parent_code as parentCode, type, value, `describe`, `seq`,update_time as updateTime from dictionary where parent_code = #{code} order by seq asc limit #{offset}, #{pageSize} ")
	List<Dictionary> getDictionaryRecordList(@Param(value="code")String code, @Param(value="offset")int offset, @Param(value="pageSize")Integer pageSize);

	/**
	 * 添加权重字典
	 * @param dict
	 * @return
	 */
	@InsertProvider(method = "addDictionary", type = JokeSqlProvider.class)
	int addDictionary(Dictionary dict);

	/**
	 * 修改权重信息
	 * @param dict
	 * @return
	 */
	@UpdateProvider(method="weightEdit",type=JokeSqlProvider.class)
	int weightEdit(Dictionary dict);

	/**
	 * 删除权限信息
	 * @param id
	 * @return
	 */
	@Delete("delete from dictionary where id = #{id}")
	int weightDel(@Param("id") Integer id);

	/**
	 * 获取字典信息
	 * @param id
	 * @return
	 */
	@Select("select id, `code`, parent_code as parentCode, `value`, `describe`, `seq` from dictionary where id= #{id}")
	Dictionary weightGet(@Param("id") String id);

	/**
	 * 获取待审核的段子列表
	 * @param type
	 * @param status
	 * @param source
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@SelectProvider(method = "getJokeListForVerify", type = JokeSqlProvider.class)
	List<Joke> getJokeListForVerify(@Param("type")Integer type, @Param("status")Integer status, @Param("source")Integer source, @Param("startDay")String startDay, @Param("endDay")String endDay, @Param("offset")Integer offset, @Param("pageSize")Integer pageSize);

	/**
	 * 获取待审核的段子列表记录总数
	 * @param type
	 * @param status
	 * @param source
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@SelectProvider(method = "getJokeListForVerifyCount", type = JokeSqlProvider.class)
	int getJokeListForVerifyCount(@Param("type")Integer type, @Param("status")Integer status, @Param("source")Integer source, @Param("startDay")String startDay, @Param("endDay")String endDay);

	/**
	 * 修改发布数量
	 * @param id
	 * @param size
	 * @return
	 */
	@Update("update channel set size = #{size} where id = #{id}")
    int editPublishSize(@Param("id")String id, @Param("size")Integer size);

	/**
	 * 查询段子列表
	 * @param type	段子类型: 0:文字、1:图片、2:动图
	 * @param count	获取段子数量
	 * @return
	 */
	@SelectProvider(method = "getPublishJokeListByType", type = JokeSqlProvider.class)
	List<Joke> getPublishJokeListByType(@Param("type")Integer type, @Param("count")Integer count);
}
