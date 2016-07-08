package com.oupeng.joke.dao.sqlprovider;

import org.apache.commons.lang3.StringUtils;

import com.oupeng.joke.domain.Topic;

public class TopicSqlProvider {
	public static String getTopicList(Integer status){
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,title,content,img,d_ids as dids,status,publish_time as publishTime,");
		sql.append(" create_time as createTime,update_time as updateTime from topic where 1 = 1 ");
		if(status != null){
			sql.append(" and status = ").append(status);
		}
		sql.append(" order by create_time desc ");
		return sql.toString();
	}
	
	public static String insertTopic(Topic topic){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into topic(title,content,img,d_ids,status,publish_time,create_time,update_time) value (");
		if(StringUtils.isNotBlank(topic.getTitle())){
			sql.append("'").append(topic.getTitle().trim()).append("',");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(topic.getContent())){
			sql.append("'").append(topic.getContent().trim()).append("',");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(topic.getImg())){
			sql.append("'").append(topic.getImg()).append("',");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(topic.getDids())){
			sql.append("'").append(topic.getDids()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("0,");
		if(topic.getPublishTimeString() != null){
			sql.append("'").append(topic.getPublishTimeString()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("now(),now())");
		return sql.toString();
	}
	
	public String updateTopic(Topic topic){
		StringBuffer sql = new StringBuffer();
		sql.append(" update topic set update_time=now(),title = ");
		if(StringUtils.isNotBlank(topic.getTitle())){
			sql.append("'").append(topic.getTitle().trim()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("content=");
		if(StringUtils.isNotBlank(topic.getContent())){
			sql.append("'").append(topic.getContent().trim()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("img=");
		if(StringUtils.isNotBlank(topic.getImg())){
			sql.append("'").append(topic.getImg()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("d_ids=");
		if(StringUtils.isNotBlank(topic.getDids())){
			sql.append("'").append(topic.getDids()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("publish_time=");
		if(StringUtils.isNotBlank(topic.getPublishTimeString())){
			sql.append("'").append(topic.getPublishTimeString()).append("'");
		}else{
			sql.append("null");
		}
		sql.append(" where id = ").append(topic.getId());
		return sql.toString();
	}
}
