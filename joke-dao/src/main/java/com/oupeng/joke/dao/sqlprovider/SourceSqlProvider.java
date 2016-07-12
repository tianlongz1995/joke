package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Source;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class SourceSqlProvider {
	/**
	 * 获取内容源列表记录总数
	 * @param source		内容源
	 * @return
	 */
	public String getSourceListCount(Source source){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from source where 1=1 ");
		if(!StringUtils.isBlank(source.getName())){
			sql.append(" and name like '%").append(source.getName()).append("%'");
		}
		if(!StringUtils.isBlank(source.getUrl())){
			sql.append(" and url like '%").append(source.getUrl()).append("%'");
		}
		if(source.getStatus() != null){
			sql.append(" and status = ").append(source.getStatus());
		}else{
			sql.append(" and status != 2");
		}
		return sql.toString();
	}
	/**
	 * 获取内容源列表
	 * @param source		内容源
	 * @return
	 */
	public String getSourceList(Source source){
		StringBuffer sql = new StringBuffer();
		sql.append("select id, name, url, status,create_time as createTime,update_time as updateTime from source where 1=1 ");
		if(!StringUtils.isBlank(source.getName())){
			sql.append(" and name like '%").append(source.getName()).append("%'");
		}
		if(!StringUtils.isBlank(source.getUrl())){
			sql.append(" and url like '%").append(source.getUrl()).append("%'");
		}
		if(source.getStatus() != null){
			sql.append(" and status = ").append(source.getStatus());
		}else{
			sql.append(" and status != 2");
		}
		sql.append(" order by create_time desc ");
		sql.append(" limit ").append(source.getOffset()).append(",").append(source.getPageSize());
		return sql.toString();
	}

	/**
	 * 新增内容源
	 * @param source
	 * @return
	 */
	public String insertSource(Source source){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into source(name, url, status,create_time,update_time) value (");
		if(!StringUtils.isBlank(source.getName())){
			sql.append("'").append(source.getName()).append("',");
		}
		if(!StringUtils.isBlank(source.getUrl())){
			sql.append("'").append(source.getUrl()).append("',");
		}
		if(source.getStatus() != null){
			sql.append(source.getStatus()).append(",");
		}else{
			sql.append("0,");
		}
		sql.append(" now(),now())");
		return sql.toString();
	}

	/**
	 * 更新内容源信息
	 * @param source
	 * @return
	 */
	public String updateSource(Source source){
		StringBuffer sql = new StringBuffer();
		sql.append(" update  source set update_time=now() ");
		if(!StringUtils.isBlank(source.getName())){
			sql.append(", name='").append(source.getName()).append("'");
		}
		if(!StringUtils.isBlank(source.getUrl())){
			sql.append(", url='").append(source.getUrl()).append("'");
		}
		sql.append(", status=").append(source.getStatus());
		sql.append(" where id = ").append(source.getId());
		return sql.toString();
	}

	/**
	 * 获取数据源抓取信息列表查询SQL语句
	 * @param map
	 * @return
	 */
	public String getSourceMonitorList(Map<String, Object> map){
		StringBuffer sql = new StringBuffer();
		sql.append("select s.name as sourceName, s.url,s.status,m.day,m.grab_count as grabCount,m.verify_rate as verifyRate,m.last_grab_time as lastGrabTime from source_monitor m left join source s on m.source_id = s.id  where 1=1");
		if(map.get("status") != null){
			sql.append(" and status = ").append(map.get("status"));
		}
		if(map.get("date") != null){
			sql.append(" and day = ").append(map.get("date"));
		}
		return sql.toString();
	}

	/**
	 * 插入内容源监控记录
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public String insertSourceMonitors(Map<String, Object> map){
		List<Integer> ids = (List<Integer>) map.get("ids");
		Integer today = (Integer) map.get("today");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into source_monitor(day, source_id, create_time, update_time) values ");
		if(today != null && ids != null){
			for(Integer id : ids){
				sql.append("(").append(today).append(",");
				sql.append(id).append(", now(), now()),");
			}
		}
		return sql.substring(0, sql.length() - 1);
	}

}
