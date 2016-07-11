package com.oupeng.joke.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.oupeng.joke.dao.sqlprovider.JokeSqlProvider;
import com.oupeng.joke.domain.Feedback;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeVerifyInfo;

public interface JokeMapper {
	
	@SelectProvider(method="getJokeList",type=JokeSqlProvider.class)
	public List<Joke> getJokeList(@Param(value="type")Integer type,@Param(value="status")Integer status,
			@Param(value="id")Integer id,@Param(value="content")String content);
	
	@UpdateProvider(method="verifyJoke",type=JokeSqlProvider.class)
	public void verifyJoke(@Param(value="status")Integer status,@Param(value="ids")String ids,@Param(value="user")String user);
	
	@Select(value="select id,title,content,img,gif,type,status,source_id as sourceId,verify_user as verifyUser,verify_time as verifyTime,"
			+ "create_time as createTime,update_time as updateTime,good,bad from joke where id = ${id}")
	@ResultType(value=Joke.class)
	public Joke getJokeById(@Param(value="id")Integer id);
	
	@UpdateProvider(method="updateJoke",type=JokeSqlProvider.class)
	public void updateJoke(Joke joke);
	
	@Select(value="select type,count(1) as num from joke where DATE_FORMAT(verify_time,'%y-%m-%d') = CURDATE() "
			+ " and status = 1 and verify_user =#{user} group by type ")
	public List<JokeVerifyInfo> getJokeVerifyInfoByUser(@Param(value="user")String user);

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
	public List<Joke> getJokeListForChannel(@Param(value="contentType")String contentType,@Param(value="start")Integer start,
			@Param(value="size")Integer size);
	
	@SelectProvider(method="getJokeCountForChannel",type=JokeSqlProvider.class)
	public int getJokeCountForChannel(String contentType);
}
