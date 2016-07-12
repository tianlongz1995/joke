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
			+ "update_time as updateTime from channel where id = #{id}")
	public Channel getChannelById(@Param(value="id")Integer id);
	
	@Select(value="update channel set update_time=now(),status = #{status} where id = #{id}")
	public void updateChannelStatus(@Param(value="id")Integer id,@Param(value="status")Integer status);
	
	@Select(value="select id,name from channel where status = 1 and type = #{type}")
	public List<Channel> getChannelByType(@Param(value="type")Integer type);
	
	@InsertProvider(method="insertChannel",type=ChannelSqlProvider.class)
	public void insertChannel(Channel channel);
	
	@UpdateProvider(method="updateChannel",type=ChannelSqlProvider.class)
	public void updateChannel(Channel channel);

	/**
	 * 获取频道状态列表
	 * @return
	 */
	@Select(value="select id,name,sort,status from (select c.id,c.name,ifnull(b.sort,100) as sort,case b.id when b.id then 1 else 0 end as status from channel c left join (select id,c_id,sort from distributor_channel where d_id = #{id}) b on c.id=b.c_id where c.status = 1) a order by a.sort asc")
	List<Channel> getChannelStatusList(@Param(value="id")Integer id);
}
