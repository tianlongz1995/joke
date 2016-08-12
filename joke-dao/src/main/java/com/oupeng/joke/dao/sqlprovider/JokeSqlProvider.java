package com.oupeng.joke.dao.sqlprovider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.oupeng.joke.domain.Joke;

public class JokeSqlProvider {

	public static String getJokeList(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		Object id = map.get("id");
		Object content = map.get("content");
		boolean isTopic = Boolean.parseBoolean(map.get("isTopic").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t1.source_id as sourceId,");
		sql.append(" t1.verify_user as verifyUser,t1.verify_time as verifyTime,t1.create_time as createTime,");
		sql.append(" t1.update_time as updateTime,t1.good,t1.bad from joke t1 where 1 = 1 ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		if(id != null){
			sql.append(" and t1.id = ").append(id).append(" ");
		}
		if(content != null && !"".equals(content)){
			sql.append(" and ( t1.content like '%").append(content).append("%' or t1.title like '%").append(content).append("%') ");
		}
		if(isTopic){
			sql.append(" and t1.verify_time >= CURDATE() and not exists ( select 1 from topic_joke t2 where t2.j_id = t1.id)");
		}
		
		sql.append(" order by t1.create_time desc limit 12 ");
		return sql.toString();
	}

	/**
	 * 更新段子状态
	 * @param map
	 * @return
	 */
	public static String updateJokeStatus(Map<String,Object> map){
		Integer status = Integer.valueOf(map.get("status").toString());
		String ids = map.get("ids").toString();
		Object user = map.get("user");
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time =now(), ");
		sql.append(" status= ").append(status);
		if(user != null && !"".equals(user)){
			sql.append(",verify_time=now(),").append("verify_user= '").append(user).append("' ");
		}
		sql.append(" where id in (").append(ids).append(")");
		return sql.toString();
	}
	
	public static String updateJoke(Joke joke){
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time =now(),verify_time=now(), ");
		sql.append(" status=1,type=").append(joke.getType()).append(",");
		sql.append(" verify_user='").append(joke.getVerifyUser()).append("',");
		if(StringUtils.isNotBlank(joke.getTitle())){
			sql.append(" title='").append(joke.getTitle().trim()).append("',");
		}else{
			sql.append(" title=null,");
		}
		if(StringUtils.isNotBlank(joke.getImg())){
			sql.append(" img='").append(joke.getImg()).append("',");
		}else{
			sql.append(" img=null,");
		}
		if(StringUtils.isNotBlank(joke.getGif())){
			sql.append(" gif='").append(joke.getGif()).append("',");
		}else{
			sql.append(" gif=null,");
		}
		if(StringUtils.isNotBlank(joke.getContent())){
			sql.append(" content='").append(joke.getContent().trim()).append("', ");
		}else{
			sql.append(" content=null, ");
		}
		if(joke.getWidth() != null){
			sql.append(" width =").append(joke.getWidth()).append(",");
		}else{
			sql.append(" width = 0, ");
		}
		if(joke.getHeight() != null){
			sql.append(" height = ").append(joke.getHeight()).append(" ");
		}else{
			sql.append(" height = 0 ");
		}
		sql.append(" where id =").append(joke.getId());
		return sql.toString();
	}
	
	public static String getJokeCountForChannel(String contentType){
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from joke where status = 1 ");
		if(StringUtils.isNotBlank(contentType)){
			sql.append(" and type in (").append(contentType).append(") ");
		}
		return sql.toString();
	}
	
	public static String getJokeListForChannel(Map<String,Object> map){
		Object contentType = map.get("contentType");
		Integer start = Integer.valueOf(map.get("start").toString());
		Integer size = Integer.valueOf(map.get("size").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,title,content,img,gif,type,verify_time as verifyTime from joke where status = 1 ");
		if(contentType != null && !"".equals(contentType)){
			sql.append(" and type in (").append(contentType).append(") ");
		}
		sql.append(" order by verify_time desc limit ").append(start).append(",").append(size);
		return sql.toString();
	}
	
	public static String getJokeListForPublish(Map<String,Object> map){
		Object lastUpdateTime = map.get("lut");
		Object currentUpdateTime = map.get("cut");
		StringBuffer sql = new StringBuffer();
		sql.append(" select  id,title,content,img,gif,type,status,source_id as sourceId,verify_user as verifyUser,verify_time as verifyTime, ");
		sql.append(" create_time as createTime,update_time as updateTime,good,bad,width,height from joke where `status` not in (0,2) ");
		if(lastUpdateTime != null){
			sql.append(" and update_time > '").append(lastUpdateTime).append("' ");
		}
		
		if(currentUpdateTime != null){
			sql.append(" and update_time <= '").append(currentUpdateTime).append("' ");
		}
		return sql.toString();
	}

	/**
	 * 存储段子
	 * @param joke
	 * @return
	 */
	public static String insertJoke(Joke joke){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into joke(title, content, img, gif, width, height, type, uuid, verify_user, status,create_time, update_time, verify_time, good) value(");
		if(StringUtils.isNotBlank(joke.getTitle())){
			sql.append("'").append(joke.getTitle().trim()).append("', ");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(joke.getContent())){
			sql.append("'").append(joke.getContent().trim()).append("', ");
		}else{
			sql.append(" null, ");
		}
		if(StringUtils.isNotBlank(joke.getImg())){
			sql.append(" '").append(joke.getImg()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(StringUtils.isNotBlank(joke.getGif())){
			sql.append(" '").append(joke.getGif()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(joke.getWidth() != null){
			sql.append(joke.getWidth()).append(", ");
		}else{
			sql.append(" 0, ");
		}
		if(joke.getHeight() != null){
			sql.append(joke.getHeight()).append(", ");
		}else{
			sql.append(" 0, ");
		}
		sql.append(joke.getType()).append(",'");
		sql.append(joke.getUuid()).append("','");
		sql.append(joke.getVerifyUser()).append("', 1, now(), now(), now(),30+FLOOR(RAND()*70) )");
		return sql.toString();
	}
}
