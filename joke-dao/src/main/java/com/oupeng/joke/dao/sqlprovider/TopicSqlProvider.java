package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.TopicCover;
import org.apache.commons.lang3.StringUtils;

import com.oupeng.joke.domain.Topic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import com.oupeng.joke.domain.Topic;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TopicSqlProvider {
	/**
	 * 获取专题列表记录总数SQL
	 * @param map
	 * @return
	 */
	public static String getTopicListCount(Map<String,Object> map){
		Object status = map.get("status");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from topic where 1 = 1 ");
		if(status != null){
			sql.append(" and status = ").append(status);
		}
		sql.append(" order by create_time desc ");
		return sql.toString();
	}
	/**
	 * 获取专题列表SQL
	 * @param map
	 * @return
	 */
	public static String getTopicList(Map<String,Object> map){
		Object status = map.get("status");
		Object offset = map.get("offset");
		Object pageSize = map.get("pageSize");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,title,content,img,d_ids as dids,status,publish_time as publishTime,");
		sql.append(" create_time as createTime,update_time as updateTime from topic where 1 = 1 ");
		if(status != null){
			sql.append(" and status = ").append(status);
		} else {
			sql.append(" and status != 2 ");
		}
		sql.append(" order by create_time desc ");
		if(offset != null && pageSize != null){
			sql.append(" limit ").append(offset).append(" , ").append(pageSize);
		}
		return sql.toString();
	}

	/**
	 * 新增专题列表
	 * @param topic
	 * @return
	 */
	public static String insertTopic(Topic topic){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into topic(title, content, img, status, publish_time, create_time, update_time) value (");
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
//		if(StringUtils.isNotBlank(topic.getDids())){
//			sql.append("'").append(topic.getDids()).append("',");
//		}else{
//			sql.append("null,");
//		}
		sql.append("0,");
		if(topic.getPublishTimeString() != null){
			sql.append("'").append(topic.getPublishTimeString()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("now(),now())");
		return sql.toString();
	}

	/**
	 * 更新专题列表
	 * @param topic
	 * @return
	 */
	public static String updateTopic(Topic topic){
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
		sql.append("publish_time=");
		if(StringUtils.isNotBlank(topic.getPublishTimeString())){
			sql.append("'").append(topic.getPublishTimeString()).append("'");
		}else{
			sql.append("null");
		}
		sql.append(" where id = ").append(topic.getId());
		return sql.toString();
	}


	/**
	 * 获取专题封面记录总数
	 * @param status
	 * @return
	 */
	public static String getTopicCoverListCount(Integer status){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from topic_cover ");
		if(status != null){
			sql.append(" where status = ").append(status);
		} else {
			sql.append(" where status != 2 ");
		}
		return sql.toString();
	}


//	/**
//	 * 获取专题封面记录总数
//	 * @param status
//	 * @return
//	 */
//	public static String getTopicCoverListCount(Integer status){
//		StringBuffer sql = new StringBuffer();
//		sql.append("select count(1) from topic_cover ");
//		if(status != null){
//			sql.append(" where status = ").append(status);
//		} else {
//			sql.append(" where status != 2 ");
//		}
//		return sql.toString();
//	}

//	/**
//	 * 获取专题封面列表
//	 * @param map
//	 * @return
//	 */
//	public static String getTopicCoverList(Map<String,Object> map){
//		StringBuffer sql = new StringBuffer();
//		sql.append("select id, name, logo, seq, status from topic_cover where 1 = 1 ");
//		Object status = map.get("status");
//		Object offset = map.get("offset");
//		Object pageSize = map.get("pageSize");
//		if(status != null){
//			sql.append(" and status = ").append(status);
//		} else {
//			sql.append(" and status != 2 ");
//		}
//		sql.append(" order by seq asc ");
//		if(offset != null && pageSize != null){
//			sql.append(" limit ").append(offset).append(" , ").append(pageSize);
//		}
//		return sql.toString();
//	}
//
//	/**
//	 * 获取所有专题封面列表
//	 * @param status
//	 * @return
//	 */
//	public static String getAllTopicCoverMoveList(Integer status){
//		StringBuffer sql = new StringBuffer();
//		sql.append("select id, name, logo, seq, status from topic_cover where 1 = 1 ");
//		if(status != null){
//			sql.append(" and status = ").append(status);
//		} else {
//			sql.append(" and status != 2 ");
//		}
//		sql.append(" order by seq asc ");
//		return sql.toString();
//	}

	/**
	 * 获取专题段子列表SQL
	 * @param map
	 * @return
	 */
	public static String getTopicJokeListByTopicId(Map<String,Object> map){
		Object topicId = map.get("topicId");
		Object offset = map.get("offset");
		Object pageSize = map.get("pageSize");
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t1.source_id as sourceId,t1.verify_user as verifyUser, t1.verify_time as verifyTime,t1.create_time as createTime,t1.update_time as updateTime,t1.good,t1.bad  from joke t1 left join topic_joke t2 on t1.id = t2.j_id where t2.status = 0 ");
		sql.append(" and t2.t_id = ").append(topicId);
		if(offset != null && pageSize != null){
			sql.append(" limit ").append(offset).append(" , ").append(pageSize);
		}
		return sql.toString();
	}
}
