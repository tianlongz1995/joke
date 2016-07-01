package com.oupeng.joke.dao.sqlprovider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.oupeng.joke.domain.Joke;

public class JokeSqlProvider {

	public static String getJokeListForVerify(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,title,content,img,gif,type,status,source_id as sourceId,");
		sql.append(" verify_user as verifyUser,verify_time as verifyTime,create_time as createTime,");
		sql.append(" update_time as updateTime,good,bad from joke where 1 = 1 ");
		if(type != null){
			sql.append(" and type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and status = ").append(status).append(" ");
		}
		sql.append(" order by create_time limit 12 ");
		return sql.toString();
	}
	
	public static String verifyJoke(Map<String,Object> map){
		Integer status = Integer.valueOf(map.get("status").toString());
		String ids = map.get("ids").toString();
		String user = map.get("user").toString();
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time =now(),verify_time=now(), ");
		sql.append(" status= ").append(status).append(",");
		sql.append(" verify_user= '").append(user).append("' ");
		sql.append(" where id in (").append(ids).append(")");
		return sql.toString();
	}
	
	public static String updateJoke(Joke joke){
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time =now(),verify_time=now(), ");
		sql.append(" status=1,type=").append(joke.getType()).append(",");
		sql.append(" verify_user='").append(joke.getVerifyUser()).append("',");
		if(StringUtils.isNotBlank(joke.getTitle())){
			sql.append(" title='").append(joke.getTitle()).append("',");
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
			sql.append(" content='").append(joke.getContent()).append("' ");
		}else{
			sql.append(" content=null ");
		}
		sql.append(" where id =").append(joke.getId());
		return sql.toString();
	}
}
