package com.oupeng.joke.dao.sqlprovider;

import java.util.Map;

public class FeedbackSqlProvider {

	/**
	 * 获取反馈数量统计列表
	 * @param map
	 * @return
	 */
	public String getDistributorFeedbackCountList(Map<String, Object> map){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.name,f.v as value from (select d_id, count(1) as v from feedback where 1=1 ");
		if(map.get("startDate") != null && map.get("endDate") != null){
			sql.append(" and create_time >= str_to_date('").append(map.get("startDate"));
			sql.append(" ','%Y-%m-%d') and create_time <= str_to_date('").append(map.get("endDate")).append("','%Y-%m-%d')");
		}
		if(map.get("distributorId") != null){
			sql.append(" and d_id = ").append(map.get("distributorId"));
		}
		sql.append(" group by d_id) f join distributor d on f.d_id=d.id and d.status = 1");
		return sql.toString();
	}

	/**
	 * 获取反馈类型列表
	 * @param map
	 * @return
	 */
	public String getDistributorFeedbackTypeList(Map<String, Object> map){
		StringBuffer sql = new StringBuffer();
		sql.append("select DATE_FORMAT(`create_time`,'%Y-%m-%d') as createTime,count(case type when 1 then 1 end) as refresh, count(case type when 2 then 2 end) as noFunny, count(case type when 3 then 3 end) as quit, count(case type when 4 then 4 end) as other from feedback where  1 = 1 ");
		if(map.get("startDate") != null && map.get("endDate") != null){
			sql.append(" and create_time >= str_to_date('").append(map.get("startDate"));
			sql.append(" ','%Y-%m-%d') and create_time <= str_to_date('").append(map.get("endDate")).append("','%Y-%m-%d')");
		}
		if(map.get("distributorId") != null){
			sql.append(" and d_id = ").append(map.get("distributorId"));
		}
		sql.append(" group by createTime");
		return sql.toString();
	}

}
