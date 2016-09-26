package com.oupeng.joke.dao.mapper;

import java.util.List;

import com.oupeng.joke.domain.TopicCover;
import org.apache.ibatis.annotations.*;

import com.oupeng.joke.dao.sqlprovider.TopicSqlProvider;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;

public interface TopicMapper {
	/**
	 * 获取专题列表
	 * @param topicCoverId
	 * @param status
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	@SelectProvider(method="getTopicList",type=TopicSqlProvider.class)
	List<Topic> getTopicList(@Param("topicCoverId")Integer topicCoverId, @Param("status")Integer status,  @Param("offset")Integer offset, @Param("pageSize")Integer pageSize);

	/**
	 * 获取专题
	 * @param id
	 * @return
	 */
	@Select(value="select id,title,content,img,d_ids as dids,status,publish_time as publishTime,"
			+ "create_time as createTime,update_time as updateTime, c_id as coverId from topic where id = #{id}")
	Topic getTopicById(@Param(value="id")Integer id);

	/**
	 * 更新专题状态
	 * @param id
	 * @param status
	 */
	@Select(value="update topic set update_time=now(),status = #{status} where id = #{id}")
	void updateTopicStatus(@Param(value="id")Integer id,@Param(value="status")Integer status);

	/**
	 * 新增专题
	 * @param topic
	 */
	@InsertProvider(method="insertTopic",type=TopicSqlProvider.class)
	void insertTopic(Topic topic);

	/**
	 * 更新专题
	 * @param topic
	 */
	@UpdateProvider(method="updateTopic",type=TopicSqlProvider.class)
	void updateTopic(Topic topic);

	/**
	 * 新增专题段子关联
	 * @param jokeId
	 * @param topicId
	 */
	@Insert(value="insert into topic_joke (t_id,j_id,status,create_time) value(#{topicId},#{jokeId},0,now())")
	void insertTopicJoke(@Param(value="jokeId")Integer jokeId,@Param(value="topicId")Integer topicId);

	/**
	 * 获取待发布的专题
	 * @return
	 */
	@Select(value="select id, title, content, img, c_id as coverId from topic where `status` = 2 and DATE_FORMAT(publish_time,'%Y-%m-%d %H') = DATE_FORMAT(now(),'%Y-%m-%d %H')")
	List<Topic> getTopicForPublish();

	/**
	 * 删除专题段子关联
	 * @param jokeId
	 * @param topicId
	 */
	@Update(value="update topic_joke set status = 1 where t_id = #{topicId} and j_id = #{jokeId} ")
	void delTopicJoke(@Param(value="jokeId")Integer jokeId,@Param(value="topicId")Integer topicId);

	/**
	 * 获取段子列表
	 * @param id 专题编号
	 * @return
	 */
	@Select(value=" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t1.source_id as sourceId,t1.verify_user as verifyUser,"
			+ " t1.verify_time as verifyTime,t1.create_time as createTime,t1.update_time as updateTime,t1.good,t1.bad "
			+ " from joke t1,topic_joke t2 where t1.id = t2.j_id and t2.status = 0 and t2.t_id = ${id} ")
	@ResultType(value=Joke.class)
	List<Joke> getJokeListByTopicId(@Param(value="id")Integer id);

	/**
	 * 获取专题封面记录总数
	 * @param status
	 * @return
	 */
	@SelectProvider(method = "getTopicCoverListCount", type = TopicSqlProvider.class)
	int getTopicCoverListCount(Integer status);

	/**
	 * 获取专题封面列表
	 * @param status
	 * @return
	 */
	@SelectProvider(method = "getTopicCoverList", type = TopicSqlProvider.class)
	List<TopicCover> getTopicCoverList(@Param(value="status")Integer status, @Param(value="offset")Integer offset, @Param(value="pageSize")Integer pageSize);

	/**
	 * 新增专题封面
	 * @param t
	 * @return
	 */
	@Insert("insert into topic_cover(seq, name, logo, status, update_by, update_time) value(#{seq}, #{name}, #{logo}, 0, #{updateBy}, now())")
	int addTopicCover(TopicCover t);

	/**
	 * 删除专题封面
	 * @param id
	 * @return
	 */
	@Delete("update topic_cover set status = 2 where id = #{id}")
	int delTopicColver(@Param("id")Integer id);

	/**
	 * 更新专题封面
	 * @param id
	 * @param seq
	 * @param name
	 * @param logo
	 * @param userName
	 * @return
	 */
	@Update("update topic_cover set seq = #{seq}, name = #{name}, logo = #{logo}, status = #{status}, update_by = #{userName},update_time = now() where id = #{id}")
	int modifyTopicCover(@Param("id")Integer id, @Param("seq")Integer seq, @Param("name")String name, @Param("logo")String logo, @Param("status")Integer status, @Param("userName")String userName);

	/**
	 * 获取所有专题封面列表
	 * @param status
	 * @return
	 */
	@SelectProvider(method = "getAllTopicCoverMoveList", type = TopicSqlProvider.class)
	List<TopicCover> getAllTopicCoverMoveList(Integer status);

	/**
	 * 更新位置
	 * @param id
	 * @param seq
	 * @return
	 */
	@Update("update topic_cover set seq = #{seq} where id = #{id}")
	void updateTopicCoverSeq(@Param("id")Integer id, @Param("seq")Integer seq);

	/**
	 * 获取专题列表记录总数
	 * @param topicCoverId
	 * @param status
	 * @return
	 */
	@SelectProvider(method = "getTopicListCount", type = TopicSqlProvider.class)
	int getTopicListCount(@Param("topicCoverId")Integer topicCoverId, @Param("status")Integer status);


	/**
	 * 获取专题段子分页列表
	 * @param topicId
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	@SelectProvider(method = "getTopicJokeListByTopicId", type = TopicSqlProvider.class)
	List<Joke> getTopicJokeListByTopicId(@Param("topicId")Integer topicId, @Param("offset")Integer offset, @Param("pageSize")Integer pageSize);

	/**
	 * 获取专题段子分页列表记录总数
	 * @param topicId
	 * @return
	 */
	@Select("select count(t1.id) from joke t1 left join topic_joke t2 on t1.id = t2.j_id where t2.status = 0 and t2.t_id = #{topicId}")
	int getTopicJokeListCountByTopicId(@Param("topicId")Integer topicId);
}
