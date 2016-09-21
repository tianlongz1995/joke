package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.QueryParam;
import com.oupeng.joke.domain.Source;
import com.oupeng.joke.domain.statistics.SourceCrawlExport;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.SelectProvider;

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
		}else{
			sql.append("NULL,");
		}
		if(!StringUtils.isBlank(source.getUrl())){
			sql.append("'").append(source.getUrl()).append("',");
		}else{
			sql.append("NULL,");
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
		sql.append("select s.name as sourceName, s.url,s.status,m.day,m.grab_count as grabCount,FORMAT(m.verify_rate * 100,2) as verifyRate,m.last_grab_time as lastGrabTime from source_monitor m left join source s on m.source_id = s.id  where 1=1");
		if(map.get("status") != null){
			sql.append(" and s.status = ").append(map.get("status"));
		}else{
			sql.append(" and s.status != 2");
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

	/**
	 * 获取内容源审核质量统计总数SQL
	 * @param queryParam
	 * @return
	 */
	public String getQualityListCount(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(c.id) from `stat_quality_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		return sql.toString();
	}


	/**
	 * 获取内容源审核质量统计记录SQL
	 * @param queryParam
	 * @return
	 */
	public String getQualityList(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.day, s.name as sourceName, s.url, c.source_type as type, passed, c.failed ,last_crawl_time as lastGrabTime from `stat_quality_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		return sql.toString();
	}

	/**
	 * 获取内容源审核质量统计总数SQL
	 * @param queryParam
	 * @return
	 */
	public String getQualityListTotalCount(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from (select c.source_id ,c.`day` from `stat_quality_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		sql.append(" group by c.day,c.source_id ) a");
		return sql.toString();
	}


	/**
	 * 获取内容源审核质量统计记录SQL
	 * @param queryParam
	 * @return
	 */
	public String getQualityTotalList(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select s.name as sourceName,s.url,cc.passed ,cc.failed, cc.lastGrabTime,cc.day from (select sum(c.`passed`) as passed,max(c.failed) as failed, c.source_id , max(c.`last_crawl_time`) as lastGrabTime,c.`day` from `stat_quality_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		sql.append("  group by c.day,c.source_id limit ");
		sql.append(queryParam.getOffset()).append(", ").append(queryParam.getPageSize());
		sql.append(" ) cc left join source s on cc.source_id = s.id order by cc.day asc ");
		return sql.toString();
	}


    /**
     * 获取内容源审核质量统计总数SQL
     * @param queryParam
     * @return
     */
    public String getSourceCrawlListCount(QueryParam queryParam){
        StringBuffer sql = new StringBuffer();
        sql.append("select count(c.id) from `stat_source_crawl_day` c left join source s on c.source_id = s.id where 1 = 1 ");
        sql.append(getSourceDayStatQuerySql(queryParam));
        return sql.toString();
    }


    /**
     * 获取内容源抓取统计总数SQL
     * @param queryParam
     * @return
     */
    public String getSourceCrawlList(QueryParam queryParam){
        StringBuffer sql = new StringBuffer();
        sql.append("select c.day, s.name as sourceName, s.url, c.source_type as type, c.grab_total as grabTotal, c.grab_count as grabCount, c.last_grab_time as lastGrabTime, c.status from `stat_source_crawl_day` c left join source s on c.source_id = s.id where 1 = 1 ");
        sql.append(getSourceDayStatQuerySql(queryParam));
        sql.append("  order by c.day,s.name desc limit ");
        sql.append(queryParam.getOffset()).append(", ").append(queryParam.getPageSize());
        return sql.toString();
    }

    /**
     * 获取内容源审核质量统计总数SQL
     * @param queryParam
     * @return
     */
    public String getSourceCrawlTotalListCount(QueryParam queryParam){
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from (select c.source_id ,c.`day` from `stat_source_crawl_day` c left join source s on c.source_id = s.id where 1 = 1 ");
        sql.append(getSourceDayStatQuerySql(queryParam));
        sql.append(" group by c.day,c.source_id ) a");
        return sql.toString();
    }


    /**
     * 获取内容源抓取统计记录SQL
     * @param queryParam
     * @return
     */
    public String getSourceCrawlTotalList(QueryParam queryParam){
        StringBuffer sql = new StringBuffer();
        sql.append("select s.name as sourceName,s.url,cc.grabTotal,cc.grabCount,cc.status,cc.lastGrabTime,cc.day from (select sum(c.`grab_total`) as grabTotal,max(c.grab_count) as grabCount, c.source_id ,min(c.status) as `status` ,max(c.`last_grab_time`) as lastGrabTime,c.`day` from `stat_source_crawl_day` c left join source s on c.source_id = s.id where 1 = 1 ");
        sql.append(getSourceDayStatQuerySql(queryParam));
        sql.append("  group by c.day,c.source_id limit ");
        sql.append(queryParam.getOffset()).append(", ").append(queryParam.getPageSize());
        sql.append(" ) cc left join source s on cc.source_id = s.id order by cc.day asc ");
        return sql.toString();
    }

	/**
	 * 获取内容源审核质量统计查询SQL
	 * @param queryParam
	 * @return
	 */
	private String getSourceDayStatQuerySql(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(queryParam.getName())){
			sql.append(" and s.name like '%").append(queryParam.getName()).append("%'");
		}
		if(queryParam.getType() != null){
			sql.append(" and c.source_type=").append(queryParam.getType());
		}
		if(StringUtils.isNumeric(queryParam.getStartTime()) && StringUtils.isNumeric(queryParam.getEndTime())){
			sql.append(" and c.day >= ").append(queryParam.getStartTime());
			sql.append(" and c.day <= ").append(queryParam.getEndTime());
		}
		return sql.toString();
	}

	/**
	 * 获取内容源分类抓取报告SQL - 分类查询
	 * @param queryParam
	 * @return
	 */
	public String getSourceCrawlExport(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.day, s.name as sourceName, s.url, case c.source_type when 0 then '纯文' when 1 then '图片' when 2 then '动图' end as type, c.grab_total as grabTotal,case c.status when 1 then '可用' else '不可用' end as status, c.grab_count as grabCount, c.last_grab_time as lastGrabTime  from `stat_source_crawl_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		sql.append(" order by c.day,s.name desc ");
		return sql.toString();
	}

	/**
	 * 获取内容源抓取报告SQL - 默认方式 - 按数据源汇总
	 * @param queryParam
	 * @return
	 */
	public String getSourceCrawlTotalExport(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select cc.day,s.name as sourceName,s.url,'-' as type,cc.grabTotal,case cc.status when 1 then '可用' else '不可用' end as status,cc.grabCount,cc.lastGrabTime from (select sum(c.`grab_total`) as grabTotal,max(c.grab_count) as grabCount, c.source_id ,min(c.status) as `status` ,max(c.`last_grab_time`) as lastGrabTime,c.`day` from `stat_source_crawl_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		sql.append("  group by c.day,c.source_id  ");
		sql.append(" ) cc left join source s on cc.source_id = s.id order by cc.day asc ");
		return sql.toString();
	}

	/**
	 * 获取内容源分类审核质量报告SQL - 分类查询
	 * @param queryParam
	 * @return
	 */
	public String getSourceQualityExport(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.day, s.name as sourceName, s.url, case c.source_type when 0 then '纯文' when 1 then '图片' when 2 then '动图' end as type,(c.passed + c.failed) as total, c.passed, c.failed ,last_crawl_time as lastGrabTime from `stat_quality_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		sql.append(" order by c.day,s.name desc ");
		return sql.toString();
	}

	/**
	 * 获取内容源审核质量报告SQL - 默认方式 - 按数据源汇总
	 * @param queryParam
	 * @return
	 */
	public String getSourceQualityTotalExport(QueryParam queryParam){
		StringBuffer sql = new StringBuffer();
		sql.append("select cc.day,s.name as sourceName,s.url,'-' as type, (cc.passed + cc.failed) as total, cc.passed ,cc.failed, cc.lastGrabTime  from (select sum(c.`passed`) as passed,max(c.failed) as failed, c.source_id , max(c.`last_crawl_time`) as lastGrabTime,c.`day` from `stat_quality_day` c left join source s on c.source_id = s.id where 1 = 1 ");
		sql.append(getSourceDayStatQuerySql(queryParam));
		sql.append(" group by c.day,c.source_id ");
		sql.append(" ) cc left join source s on cc.source_id = s.id order by cc.day,sourceName asc ");
		return sql.toString();
	}
}
