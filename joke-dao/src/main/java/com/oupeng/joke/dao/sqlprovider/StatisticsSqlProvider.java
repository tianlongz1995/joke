package com.oupeng.joke.dao.sqlprovider;

import java.util.Map;

public class StatisticsSqlProvider {
	
	public static String getDayTotalCount(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_total_day where 1 = 1 ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		return sql.toString();
	}
	
	public static String getDayTotalList(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select day as time ,pv,uv from stat_total_day where 1 = 1 ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		sql.append(" order by day desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}
	
	public static String getWeekTotalCount(Map<String,Object> map){
		Object startWeek = map.get("startWeek");
		Object endWeek = map.get("endWeek");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_total_week where 1 = 1 ");
		if(startWeek != null && !"".equals(startWeek)){
			sql.append(" and week >= ").append(startWeek);
		}
		if(endWeek != null && !"".equals(endWeek)){
			sql.append(" and week <= ").append(endWeek);
		}
		return sql.toString();
	}
	
	public static String getWeekTotalList(Map<String,Object> map){
		Object startWeek = map.get("startWeek");
		Object endWeek = map.get("endWeek");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select week as time ,pv,uv from stat_total_week where 1 = 1 ");
		if(startWeek != null && !"".equals(startWeek)){
			sql.append(" and week >= ").append(startWeek);
		}
		if(endWeek != null && !"".equals(endWeek)){
			sql.append(" and week <= ").append(endWeek);
		}
		sql.append(" order by week desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}
	
	public static String getMonthTotalCount(Map<String,Object> map){
		Object startMonth = map.get("startMonth");
		Object endMonth = map.get("endMonth");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_total_month where 1 = 1 ");
		if(startMonth != null && !"".equals(startMonth)){
			sql.append(" and month >= ").append(startMonth);
		}
		if(endMonth != null && !"".equals(endMonth)){
			sql.append(" and month <= ").append(endMonth);
		}
		return sql.toString();
	}
	
	public static String getMonthTotalList(Map<String,Object> map){
		Object startMonth = map.get("startMonth");
		Object endMonth = map.get("endMonth");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select month as time ,pv,uv from stat_total_month where 1 = 1 ");
		if(startMonth != null && !"".equals(startMonth)){
			sql.append(" and month >= ").append(startMonth);
		}
		if(endMonth != null && !"".equals(endMonth)){
			sql.append(" and month <= ").append(endMonth);
		}
		sql.append(" order by month desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}
	
	public static String getDayDetailCount(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_detail_day where did != 'N' ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		sql.append(getAndSql(map));
		return sql.toString();
	}
	
	public static String getDayDetailList(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select day as time,did,cid,t_pv as totalPv,t_un as totalUv,e_pv as enterPv,e_un as enterUv,l_pv as listPv,l_un as listUv,");
		sql.append(" d_pv as detailPv,d_un as detailUv,o_pv as oldUserPv,o_un as oldUserUv,n_pv as newUserPv,n_un as newUserUv,y_o_un as ");
		sql.append(" oldUserKeep,y_n_un as newUserKeep,y_a_un as actionUserKeep,a_un as actionUserUv, ");
		sql.append(" p_n_un as lastNewUserUv,p_o_un as lastOldUserUv,p_a_un as lastActionUserUv from stat_detail_day where did != 'N' ");

		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		sql.append(getAndSql(map));
		sql.append(" order by day desc,did desc,cid desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}
	
	public static String getDayDetailList4Export(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		StringBuffer sql = new StringBuffer();
		sql.append(" select day as time,did,cid,t_pv as totalPv,t_un as totalUv,e_pv as enterPv,e_un as enterUv,l_pv as listPv,l_un as listUv,");
		sql.append(" d_pv as detailPv,d_un as detailUv,o_pv as oldUserPv,o_un as oldUserUv,n_pv as newUserPv,n_un as newUserUv,y_o_un as ");
		sql.append(" oldUserKeep,y_n_un as newUserKeep,y_a_un as actionUserKeep,a_un as actionUserUv, ");
		sql.append(" p_n_un as lastNewUserUv,p_o_un as lastOldUserUv,p_a_un as lastActionUserUv from stat_detail_day where did != 'N' ");

		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		sql.append(getAndSql(map));
		sql.append(" order by day desc,did desc,cid desc ");
		return sql.toString();
	}
	
	public static String getWeekDetailCount(Map<String,Object> map){
		Object startWeek = map.get("startWeek");
		Object endWeek = map.get("endWeek");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_detail_week where did != 'N' ");
		if(startWeek != null && !"".equals(startWeek)){
			sql.append(" and week >= ").append(startWeek);
		}
		if(endWeek != null && !"".equals(endWeek)){
			sql.append(" and week <= ").append(endWeek);
		}
		sql.append(getAndSql(map));
		return sql.toString();
	}
	
	public static String getWeekDetailList(Map<String,Object> map){
		Object startWeek = map.get("startWeek");
		Object endWeek = map.get("endWeek");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select week as time,did,cid,t_pv as totalPv,t_un as totalUv,e_pv as enterPv,e_un as enterUv,l_pv as listPv,l_un as listUv,");
		sql.append(" d_pv as detailPv,d_un as detailUv,o_pv as oldUserPv,o_un as oldUserUv,n_pv as newUserPv,n_un as newUserUv,y_o_un as ");
		sql.append(" oldUserKeep,y_n_un as newUserKeep,y_a_un as actionUserKeep,a_un as actionUserUv, ");
		sql.append(" p_n_un as lastNewUserUv,p_o_un as lastOldUserUv,p_a_un as lastActionUserUv  from stat_detail_week where did != 'N' ");

		if(startWeek != null && !"".equals(startWeek)){
			sql.append(" and week >= ").append(startWeek);
		}
		if(endWeek != null && !"".equals(endWeek)){
			sql.append(" and week <= ").append(endWeek);
		}
		sql.append(getAndSql(map));
		sql.append(" order by week desc,did desc,cid desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}
	
	public static String getMonthDetailCount(Map<String,Object> map){
		Object startMonth = map.get("startMonth");
		Object endMonth = map.get("endMonth");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_detail_month where did != 'N' ");
		if(startMonth != null && !"".equals(startMonth)){
			sql.append(" and month >= ").append(startMonth);
		}
		if(endMonth != null && !"".equals(endMonth)){
			sql.append(" and month <= ").append(endMonth);
		}
		sql.append(getAndSql(map));
		return sql.toString();
	}
	
	public static String getMonthDetailList(Map<String,Object> map){
		Object startMonth = map.get("startMonth");
		Object endMonth = map.get("endMonth");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select month as time,did,cid,t_pv as totalPv,t_un as totalUv,e_pv as enterPv,e_un as enterUv,l_pv as listPv,l_un as listUv,");
		sql.append(" d_pv as detailPv,d_un as detailUv,o_pv as oldUserPv,o_un as oldUserUv,n_pv as newUserPv,n_un as newUserUv,y_o_un as ");
		sql.append(" oldUserKeep,y_n_un as newUserKeep,y_a_un as actionUserKeep,a_un as actionUserUv, ");
		sql.append(" p_n_un as lastNewUserUv,p_o_un as lastOldUserUv,p_a_un as lastActionUserUv  from stat_detail_month where did != 'N' ");

		if(startMonth != null && !"".equals(startMonth)){
			sql.append(" and month >= ").append(startMonth);
		}
		if(endMonth != null && !"".equals(endMonth)){
			sql.append(" and month <= ").append(endMonth);
		}
		sql.append(getAndSql(map));
		sql.append(" order by month desc,did desc,cid desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}
	
	private static StringBuffer getAndSql(Map<String,Object> map){
		Object distributorIds = map.get("dids");
		Object channelIds = map.get("cids");
		Integer type = Integer.valueOf(map.get("type").toString());
		StringBuffer sql = new StringBuffer();
		if(type == 1){
			if(distributorIds != null && !"".equals(distributorIds)){
				sql.append(" and did in (").append(distributorIds).append(") ");
			}
			
			if(channelIds != null && !"".equals(channelIds)){
				sql.append(" and cid in (").append(channelIds).append(") ");
			}else{
				sql.append(" and cid = 'total' ");
			}
		}else if(type == 2){
			if(channelIds != null && !"".equals(channelIds)){
				sql.append(" and cid in (").append(channelIds).append(") ");
			}
			
			if(distributorIds != null && !"".equals(distributorIds)){
				sql.append(" and did in (").append(distributorIds).append(") ");
			}else{
				sql.append(" and did = 'total' ");
			}
		}else if(distributorIds != null && !"".equals(distributorIds)){
			sql.append(" and did in (").append(distributorIds).append(") ");
			if(channelIds != null && !"".equals(channelIds)){
				sql.append(" and cid in (").append(channelIds).append(") ");
			}
		}else if(channelIds != null && !"".equals(channelIds)){
			sql.append(" and cid in (").append(channelIds).append(") ");
		}
		return sql;
	}

	/**
	 * 获取下拉刷新日报表总数查询SQL
	 * @param map
	 * @return
	 */
	public static String getDayDropTotalCount(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Object flushType = map.get("flushType");
		Object dateType = map.get("dateType");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_drop_total_day where 1 = 1 ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		if(flushType != null && !"".equals(flushType)){
			sql.append(" and type = ").append(flushType);
		}
		if(dateType != null && !"".equals(dateType)){
			sql.append(" and date_type = ").append(dateType);
		}
		return sql.toString();
	}

	/**
	 * 获取下拉刷新日报表记录查询SQL
	 * @param map
	 * @return
	 */
	public static String getDayDropTotalList(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Object flushType = map.get("flushType");
		Object dateType = map.get("dateType");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select day as time ,pv,uv from stat_drop_total_day where 1 = 1 ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		if(flushType != null && !"".equals(flushType)){
			sql.append(" and type = ").append(flushType);
		}
		if(dateType != null && !"".equals(dateType)){
			sql.append(" and date_type = ").append(dateType);
		}
		sql.append(" order by day desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}

	/**
	 * 获取下拉刷新日报明细总数查询SQL
	 * @param map
	 * @return
	 */
	public static String getDropDayDetailCount(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Object flushType = map.get("flushType");
		Object dateType = map.get("dateType");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_drop_detail_day s left join `distributor` d on s.did=d.id left join channel c on s.cid=c.id where s.did != 'N' ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and s.day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and s.day <= ").append(endDay);
		}
		if(flushType != null && !"".equals(flushType)){
			sql.append(" and s.type = ").append(flushType);
		}
		if(dateType != null && !"".equals(dateType)){
			sql.append(" and s.date_type = ").append(dateType);
		}
		sql.append(getAsAndSql("s.", map));
		return sql.toString();
	}

	/**
	 * 获取下拉刷新日报明细记录查询SQL
	 * @param map
	 * @return
	 */
	public static String getDropDayDetailList(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Object flushType = map.get("flushType");
		Object dateType = map.get("dateType");
		Integer start = Integer.parseInt(map.get("start").toString());
		Integer end = Integer.parseInt(map.get("end").toString());
		StringBuffer sql = new StringBuffer();
		sql.append("select s.day as time,d.name dName,c.name cName,s.pv as totalPv,s.uv as totalUv from stat_drop_detail_day s left join `distributor` d on s.did=d.id left join channel c on s.cid=c.id where s.did != 'N' ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and s.day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and s.day <= ").append(endDay);
		}
		if(flushType != null && !"".equals(flushType)){
			sql.append(" and s.type = ").append(flushType);
		}
		if(dateType != null && !"".equals(dateType)){
			sql.append(" and s.date_type = ").append(dateType);
		}
		sql.append(getAsAndSql("s.", map));
		sql.append(" order by s.day desc,s.did desc,s.cid desc ");
		sql.append(" limit ").append(start).append(",").append(end);
		return sql.toString();
	}

	/**
	 * 获取带别名的SQL
	 * @param map
	 * @return
	 */
	private static StringBuffer getAsAndSql(String as, Map<String,Object> map){
		Object distributorIds = map.get("dids");
		Object channelIds = map.get("cids");
		Integer type = Integer.valueOf(map.get("type").toString());
		StringBuffer sql = new StringBuffer();
		if(type == 1){
			if(distributorIds != null && !"".equals(distributorIds)){
				sql.append(" and ").append(as).append("did in (").append(distributorIds).append(") ");
			}

			if(channelIds != null && !"".equals(channelIds)){
				sql.append(" and ").append(as).append("cid in (").append(channelIds).append(") ");
			}else{
				sql.append(" and ").append(as).append("cid = 'total' ");
			}
		}else if(type == 2){
			if(channelIds != null && !"".equals(channelIds)){
				sql.append(" and ").append(as).append("cid in (").append(channelIds).append(") ");
			}

			if(distributorIds != null && !"".equals(distributorIds)){
				sql.append(" and ").append(as).append("did in (").append(distributorIds).append(") ");
			}else{
				sql.append(" and ").append(as).append("did = 'total' ");
			}
		}else if(distributorIds != null && !"".equals(distributorIds)){
			sql.append(" and ").append(as).append("did in (").append(distributorIds).append(") ");
			if(channelIds != null && !"".equals(channelIds)){
				sql.append(" and ").append(as).append("cid in (").append(channelIds).append(") ");
			}
		}else if(channelIds != null && !"".equals(channelIds)){
			sql.append(" and ").append(as).append("cid in (").append(channelIds).append(") ");
		}
		return sql;
	}

	/**
	 * 获取图片展开点击统计总记录数SQL
	 * @param map
	 * @return
	 */
	public static String getUsageCount(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Object reportType = map.get("reportType"); 	// 报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
		Object dateType = map.get("dateType");		// 日期类型: 0:日报; 1:周报; 2:月报
		Object logType = map.get("logType");		// 日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
		Object distributorId = map.get("distributorId");
		Object channelId = map.get("channelId");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from stat_usage where 1 = 1 ");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and day <= ").append(endDay);
		}
		if(distributorId != null && !"".equals(distributorId)){
			sql.append(" and did = '").append(distributorId).append("'");
		}
		if(channelId != null && !"".equals(channelId)){
			sql.append(" and cid = '").append(channelId).append("'");
		}
//		日期类型: 0:日报; 1:周报; 2:月报
		if(dateType != null && !"".equals(dateType)){
			sql.append(" and date_type = ").append(dateType);
		}
//		报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
		if(reportType != null && !"".equals(reportType)){
			sql.append(" and report_type = ").append(reportType);
		}
//		日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
		sql.append(" and log_type = ").append(logType);
		return sql.toString();
	}

	/**
	 * 获取图片展开点击统计记录列表
	 * @param map
	 * @return
	 */
	public static String getUsageList(Map<String,Object> map){
		Object startDay = map.get("startDay");
		Object endDay = map.get("endDay");
		Object reportType = map.get("reportType"); 	// 报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
		Object dateType = map.get("dateType");		// 日期类型: 0:日报; 1:周报; 2:月报
		Object logType = map.get("logType");		// 日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
		Object distributorId = map.get("distributorId");
		Object channelId = map.get("channelId");
		Integer offset = Integer.parseInt(map.get("offset").toString());
		Integer pageSize = Integer.parseInt(map.get("pageSize").toString());
		StringBuffer sql = new StringBuffer();
		sql.append("select s.day as time, d.name as dName, c.name as cName, s.pv as totalPv, s.uv as totalUv from stat_usage s left join `distributor` d on s.did = d.id left join channel c on s.cid = c.id where 1 = 1");
		if(startDay != null && !"".equals(startDay)){
			sql.append(" and s.day >= ").append(startDay);
		}
		if(endDay != null && !"".equals(endDay)){
			sql.append(" and s.day <= ").append(endDay);
		}
		if(distributorId != null && !"".equals(distributorId)){
			sql.append(" and s.did = '").append(distributorId).append("'");
		}
		if(channelId != null && !"".equals(channelId)){
			sql.append(" and s.cid = '").append(channelId).append("'");
		}
//		日期类型: 0:日报; 1:周报; 2:月报
		if(dateType != null && !"".equals(dateType)){
			sql.append(" and s.date_type = ").append(dateType);
		}
//		报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
		if(reportType != null && !"".equals(reportType)){
			sql.append(" and s.report_type = ").append(reportType);
		}
//		日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
		sql.append(" and s.log_type = ").append(logType);
		sql.append(" order by s.day, s.did, s.cid desc ");
		sql.append(" limit ").append(offset).append(", ").append(pageSize);
		return sql.toString();
	}

}
