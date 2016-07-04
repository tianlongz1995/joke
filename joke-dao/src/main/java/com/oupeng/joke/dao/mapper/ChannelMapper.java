package com.oupeng.joke.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.oupeng.joke.dao.sqlprovider.ChannelSqlProvider;
import com.oupeng.joke.domain.Channel;

public interface ChannelMapper {
	
	@SelectProvider(method="getChannelList",type=ChannelSqlProvider.class)
	public List<Channel> getChannelList(Integer status);
	
	@Select(value="select id,name,type,status,good,bad,content_type as contentType,create_time as createTime,"
			+ "update_time as updateTime from channel where id = ${id}")
	public Channel getChannelById(Integer id);
	
	@Select(value="update channel set update_time=now(),status = #{status},good=#{good},bad=#{bad} where id = ${id}")
	public void updateChannelStatus(@Param(value="id")Integer id,@Param(value="status")Integer status,
			@Param(value="good")Integer good,@Param(value="bad")Integer bad);
	
	@Select(value="select id,name from channel where status = 1 and type = #{type}")
	public Channel getChannelByType(Integer type);
	
	@InsertProvider(method="insertChannel",type=ChannelSqlProvider.class)
	public void insertChannel(Channel channel);
	
	@UpdateProvider(method="updateChannel",type=ChannelSqlProvider.class)
	public void updateChannel(Channel channel);
}
