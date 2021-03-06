package com.oupeng.joke.dao.mapper;

import java.util.List;

import com.oupeng.joke.domain.ChannelMenu;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.oupeng.joke.dao.sqlprovider.ChannelSqlProvider;
import com.oupeng.joke.domain.Channel;

public interface ChannelMapper {

	/**
	 * 获取频道列表
	 * @param status
	 * @return
	 */
	@SelectProvider(method="getChannelList",type=ChannelSqlProvider.class)
	List<Channel> getChannelList(Integer status);

	/**
	 * 获取频道
	 * @param id
	 * @return
	 */
	@Select(value="select id,name,type,status,good,bad,content_type as contentType,create_time as createTime,"
			+ "update_time as updateTime,size from channel where id = #{id}")
	Channel getChannelById(@Param(value="id")Integer id);
	
	@Select(value="update channel set update_time=now(),status = #{status} where id = #{id}")
	void updateChannelStatus(@Param(value="id")Integer id,@Param(value="status")Integer status);
	
	@Select(value="select id,name from channel where status = 1 and type = #{type}")
	List<Channel> getChannelByType(@Param(value="type")Integer type);

	/**
	 * 新增频道
	 * @param channel
	 */
	@InsertProvider(method="insertChannel",type=ChannelSqlProvider.class)
	void insertChannel(Channel channel);

	/**
	 * 修改频道
	 * @param channel
	 */
	@UpdateProvider(method="updateChannel",type=ChannelSqlProvider.class)
	void updateChannel(Channel channel);

	/**
	 * 获取频道状态列表
	 * @return
	 */
	@Select(value="select id,name,sort,status from (select c.id,c.name,ifnull(b.sort,100) as sort,case b.id when b.id then 1 else 0 end as status from channel c left join (select id,c_id,sort from distributor_channel where d_id = #{id}) b on c.id=b.c_id where c.status = 1) a order by a.sort asc")
	List<Channel> getChannelStatusList(@Param(value="id")Integer id);
	/**
	 * 获取渠道下频道列表
	 * @param id
	 * @return
	 */
	@Select(value="select c.id as i,c.name as n,c.type as t from distributor_channel dc left join channel c on dc.c_id=c.id where dc.d_id = #{id} and c.status =1 order by dc.sort asc")
	List<ChannelMenu> getDistributorChannelList(@Param(value="id")Integer id);
	
	@Select(value="SELECT id FROM channel WHERE name like '%${name}%'")
	public List<Integer> getChannelIdListByName(@Param(value = "name")String name);

}
