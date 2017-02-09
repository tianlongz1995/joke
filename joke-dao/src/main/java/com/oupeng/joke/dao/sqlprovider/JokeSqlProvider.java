package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Dictionary;
import com.oupeng.joke.domain.Joke;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class JokeSqlProvider {

	/**
	 * 获取专题段子列表总数SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForTopicCount(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(t1.id) from joke t1 where t1.verify_time >= CURDATE()  ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		sql.append(" and not exists (select 1 from topic_joke t2 where t2.j_id = t1.id) ");
		return sql.toString();
	}
	/**
	 * 获取专题段子列表总数SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForTopic(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		Object offset = map.get("offset");
		Object pageSize = map.get("pageSize");
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
		sql.append(" and t1.verify_time >= CURDATE() and not exists ( select 1 from topic_joke t2 where t2.j_id = t1.id) ");
		sql.append(" order by t1.create_time desc ");
		if(offset != null && pageSize != null){
			sql.append(" limit ").append(offset).append(", ").append(pageSize);
		}
		return sql.toString();
	}
	/**
	 * 获取数据源列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeList(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		Object id = map.get("id");
		Object content = map.get("content");
		boolean isTopic = Boolean.parseBoolean(map.get("isTopic").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t1.source_id as sourceId,");
		sql.append(" t1.verify_user as verifyUser,t1.verify_time as verifyTime,t1.create_time as createTime,");
		sql.append(" t1.update_time as updateTime,t1.good,t1.bad,t1.weight from joke t1 where 1 = 1  ");
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
//		t1.create_time desc ,
		sql.append(" order by t1.weight desc limit 12 ");
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
		sql.append(" ,audit = ").append(status);
		if(user != null && !"".equals(user)){
			sql.append(",verify_time=now(),").append("verify_user= '").append(user).append("' ");
		}
		sql.append(" where id in (").append(ids).append(")");
		return sql.toString();
	}

	/**
	 * 更新段子信息SQL
	 * @param joke
	 * @return
	 */
	public static String updateJoke(Joke joke){
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time =now(),verify_time=now(), ");
//		getType 为 null
//		sql.append(" status=1,type=").append(joke.getType()).append(",");
		sql.append(" status=1,");
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
			sql.append(" height = ").append(joke.getHeight()).append(",");
		}else{
			sql.append(" height = 0, ");
		}
		if (joke.getWeight() != null) {
			sql.append(" weight = ").append(joke.getWeight());
		}else{
			sql.append(" weight = 0");
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

	/**
	 * 获取频道数据源列表SQL
	 * @param map
	 * @return
	 */
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

	/**
	 * 获取待发布的段子列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForPublish(Map<String,Object> map){
		Object lastUpdateTime = map.get("lut");
		Object currentUpdateTime = map.get("cut");
		StringBuffer sql = new StringBuffer();
		sql.append(" select  id,title,content,img,gif,type,status,source_id as sourceId,verify_user as verifyUser,verify_time as verifyTime, ");
		sql.append(" create_time as createTime,update_time as updateTime,good,bad,width,height,comment_number as commentNumber, comment as commentContent, avata, nick, src from joke where `status` not in (0,2) ");
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

	/**
	 * 添加权重字典
	 * @param dict
	 * @return
	 */
	public static String addDictionary(Dictionary dict){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into dictionary(`code`, parent_code, `type`, `value`, `describe`, `seq`, create_time, update_time) value(");
		if(StringUtils.isNotBlank(dict.getCode())){
			sql.append("'").append(dict.getCode().trim()).append("', ");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(dict.getParentCode())){
			sql.append("'").append(dict.getParentCode().trim()).append("', ");
		}else{
			sql.append(" null, ");
		}
		if(StringUtils.isNotBlank(dict.getParentCode())){
			sql.append(" '").append(dict.getParentCode()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(StringUtils.isNotBlank(dict.getValue())){
			sql.append(" '").append(dict.getValue()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(StringUtils.isNotBlank(dict.getDescribe())){
			sql.append(" '").append(dict.getDescribe()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(dict.getSeq() != null){
			sql.append(dict.getSeq()).append(", ");
		}else{
			sql.append(" 0, ");
		}
		sql.append(" now(), now() )");
		return sql.toString();
	}

	/**
	 * 修改权重字典
	 * @param dict
	 * @return
	 */
	public static String weightEdit(Dictionary dict){
		StringBuffer sql = new StringBuffer();
		sql.append(" update dictionary set update_time =now(), ");
		if(StringUtils.isNotBlank(dict.getCode())){
			sql.append(" `code`='").append(dict.getCode().trim()).append("',");
		}else{
			sql.append(" `code`=null,");
		}
		if(StringUtils.isNotBlank(dict.getParentCode())){
			sql.append(" parent_code='").append(dict.getParentCode()).append("',");
		}else{
			sql.append(" parent_code=null,");
		}
		if(StringUtils.isNotBlank(dict.getValue())){
			sql.append(" `value`='").append(dict.getValue()).append("',");
		}else{
			sql.append(" `value`=null,");
		}
		if(StringUtils.isNotBlank(dict.getDescribe())){
			sql.append(" `describe`='").append(dict.getDescribe().trim()).append("'");
		}else{
			sql.append(" `describe`=null ");
		}
		sql.append(" where id =").append(dict.getId());
		return sql.toString();
	}

	/**
	 * 获取待审核的段子列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForVerify(Map<String,Object> map){
		Integer type = (Integer) map.get("type");
		Integer status = (Integer) map.get("status");
		Integer source = (Integer) map.get("source");
		String startDay = (String) map.get("startDay");
		String endDay = (String) map.get("endDay");
		Integer offset = (Integer) map.get("offset");
		Integer pageSize = (Integer) map.get("pageSize");
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t2.name as sourceName,");
		sql.append(" t1.verify_user as verifyUser,t1.verify_time as verifyTime,t1.create_time as createTime,");
		sql.append(" t1.update_time as updateTime,t1.weight from joke t1 left join `source` t2 on t1.source_id = t2.`id` where 1 = 1 ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		if(source != null){
			sql.append(" and t1.source_id = ").append(source).append(" ");
		}
		if(StringUtils.isNotBlank(startDay)){
			sql.append(" and t1.`create_time` >= str_to_date('").append(startDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
		if(StringUtils.isNotBlank(endDay)){
			sql.append(" and t1.`create_time` <= str_to_date('").append(endDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
//		t1.create_time desc
		sql.append(" order by  t1.weight desc limit ");
		sql.append(offset).append(" , ").append(pageSize);

		System.out.println(sql.toString());
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		return sql.toString();
	}

	/**
	 * 获取待审核的段子列表记录总数SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForVerifyCount(Map<String,Object> map){
		Integer type = (Integer) map.get("type");
		Integer status = (Integer) map.get("status");
		Integer source = (Integer) map.get("source");
		String startDay = (String) map.get("startDay");
		String endDay = (String) map.get("endDay");

		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from joke t1 left join `source` t2 on t1.source_id = t2.`id` where 1 = 1 ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		if(source != null){
			sql.append(" and t1.source_id = ").append(source).append(" ");
		}
		if(StringUtils.isNotBlank(startDay)){
			sql.append(" and t1.`create_time` >= str_to_date('").append(startDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
		if(StringUtils.isNotBlank(endDay)){
			sql.append(" and t1.`create_time` <= str_to_date('").append(endDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
		return sql.toString();
	}

	/**
	 * 获取段子列表
	 * @param map
	 * @return
	 */
	public static String getPublishJokeListByType(Map<String,Object> map){
		Object type = map.get("type");
		Object count = map.get("count");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id, update_time as updateTime from joke t1 where type = ").append(type);
		sql.append(" and status = 3 order by update_time desc ");
		if(count != null){
			sql.append(" limit ").append(count);
		}
		return sql.toString();
	}

	/**
	 * 查询推荐频道下数据内容
	 * @param map
	 * @return
	 */
	public static String getJokeListForPublishRecommend(Map<String,Object> map){
		Object type = map.get("type");
		Object num = map.get("num");
		StringBuffer sql = new StringBuffer();
		sql.append("select id FROM joke t where `status` = 3 and `type` =  ").append(type);
		sql.append(" and DATE_FORMAT(update_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) ");
		sql.append(" and not EXISTS ( select 1 from topic_joke where j_id = t.id) ORDER BY good desc ");
		if(num != null){
			sql.append(" limit ").append(num);
		}
		return sql.toString();
	}

	/**
	 * 查询最近审核通过的数据
	 * @param map
	 * @return
	 */
	public static String getJokeForPublishChannel(Map<String,Object> map){
		Object contentType = map.get("contentType");
		Object size        = map.get("size");
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.id,t.verify_time as verifyTime from joke t where t.`status` = 1 and ");
		sql.append(" t.type in ( ").append(contentType).append(" )");
		sql.append(" and not EXISTS ( select 1 from topic_joke where j_id = t.id) order by t.verify_time desc ");
		if(size != null) {
			sql.append(" limit ").append(size);
		}
		return sql.toString();
	}

	/**
	 * 添加发布规则
	 * @param map
	 * @return
	 */
	public static String addPublishRole(Map<String,Object> map){
		Object code = map.get("code");
		Object value = map.get("value");
		StringBuffer sql = new StringBuffer();
		sql.append(" update dictionary set update_time = now(),");
		if(null != value){
			sql.append("  value= '").append(value).append("' ");
		}else{
			sql.append(" value = null,");
		}
		sql.append("where code = ").append(code);
		return sql.toString();
	}

}
