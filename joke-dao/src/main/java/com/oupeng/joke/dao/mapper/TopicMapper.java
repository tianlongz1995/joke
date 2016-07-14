package com.oupeng.joke.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.oupeng.joke.dao.sqlprovider.TopicSqlProvider;
import com.oupeng.joke.domain.Topic;

public interface TopicMapper {
	
	@SelectProvider(method="getTopicList",type=TopicSqlProvider.class)
	public List<Topic> getTopicList(Integer status);
	
	@Select(value="select id,title,content,img,d_ids as dids,status,publish_time as publishTime,"
			+ "create_time as createTime,update_time as updateTime from topic where id = #{id}")
	public Topic getTopicById(@Param(value="id")Integer id);
	
	@Select(value="update topic set update_time=now(),status = #{status} where id = #{id}")
	public void updateTopicStatus(@Param(value="id")Integer id,@Param(value="status")Integer status);
	
	@InsertProvider(method="insertTopic",type=TopicSqlProvider.class)
	public void insertTopic(Topic topic);
	
	@UpdateProvider(method="updateTopic",type=TopicSqlProvider.class)
	public void updateTopic(Topic topic);
	
	@Insert(value="insert into topic_joke (t_id,j_id,status,create_time) value(#{topicId},#{jokeId},0,now())")
	public void insertTopicJoke(@Param(value="jokeId")Integer jokeId,@Param(value="topicId")Integer topicId);
	
	@Select(value=" select id,title,content,img,d_ids as dids from topic where `status` = 2 and "
			+ " DATE_FORMAT(publish_time,'%Y-%m-%d %H') = DATE_FORMAT(now(),'%Y-%m-%d %H')")
	public List<Topic> getTopicForPublish();
}