package com.oupeng.joke.dao.sqlprovider;

import org.apache.commons.lang3.StringUtils;

import com.oupeng.joke.domain.Channel;

public class ChannelSqlProvider {
	/**
	 * 获取频道列表SQL
	 * @param status
	 * @return
	 */
	public static String getChannelList(Integer status){
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name,type,status,good,bad,content_type as contentType,create_time as createTime,");
		sql.append(" update_time as updateTime,size from channel where 1 = 1 ");
		if(status != null){
			sql.append(" and status = ").append(status);
		}
		return sql.toString();
	}

	/**
	 * 新增频道
	 * @param channel
	 * @return
	 */
	public static String insertChannel(Channel channel){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into channel(name,type,status,content_type,size,good,bad,create_time,update_time) value (");
		if(StringUtils.isNotBlank(channel.getName())){
			sql.append("'").append(channel.getName().trim()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append(channel.getType()).append(",0,");
		if(StringUtils.isNotBlank(channel.getContentType())){
			sql.append("'").append(channel.getContentType()).append("',");
		}else{
			sql.append("null,");
		}
		if(channel.getSize() != null){
			sql.append(channel.getSize()).append(",");
		}else{
			sql.append("100,");
		}
		sql.append("0,0,now(),now())");
		return sql.toString();
	}

	/**
	 * 修改频道SQL
	 * @param channel
	 * @return
	 */
	public String updateChannel(Channel channel){
		StringBuffer sql = new StringBuffer();
		sql.append(" update channel set update_time=now(),name = ");
		if(StringUtils.isNotBlank(channel.getName())){
			sql.append("'").append(channel.getName().trim()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("type=").append(channel.getType());
		sql.append(",content_type=");
		if(StringUtils.isNotBlank(channel.getContentType())){
			sql.append("'").append(channel.getContentType()).append("', ");
		}else{
			sql.append("null, ");
		}
		sql.append("size=");
		if(channel.getSize() != null){
			sql.append(channel.getSize()).append(" ");
		}else{
			sql.append("100 ");
		}
		sql.append(" where id = ").append(channel.getId());
		return sql.toString();
	}
}
