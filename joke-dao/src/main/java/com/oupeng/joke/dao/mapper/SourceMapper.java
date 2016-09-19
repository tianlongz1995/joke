package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.SourceSqlProvider;
import com.oupeng.joke.domain.QueryParam;
import com.oupeng.joke.domain.Source;
import com.oupeng.joke.domain.SourceCrawl;
import com.oupeng.joke.domain.statistics.SourceCrawlExport;
import com.oupeng.joke.domain.statistics.SourceQualityExport;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SourceMapper {
	/**
	 * 获取内容源列表总数
	 * @param source
	 * @return
	 */
	@SelectProvider(method="getSourceListCount",type=SourceSqlProvider.class)
	int getSourceListCount(Source source);
	/**
	 * 获取内容源列表
	 * @param source        内容源
	 * @return
	 */
	@SelectProvider(method="getSourceList",type=SourceSqlProvider.class)
	List<Source> getSourceList(Source source);
	/**
	 * 获取内容源列表
	 * @return
	 */
	@Select("select id, name from source")
	List<Source> getAllSourceList();

	/**
	 * 获取内容源信息
	 * @param id 内容源编号
	 * @return
	 */
	@Select(value="select id, name, url, status,create_time as createTime,update_time as updateTime from source where id = #{id}")
	Source getSourceById(@Param(value = "id") Integer id);

	/**
	 * 删除（逻辑删除）
	 * @param id
	 */
	@Update(value="update source set update_time=now(),status = 2 where id = #{id}")
	void del(@Param(value = "id") Integer id);


	/**
	 * 新增内容源
	 * @param source
	 */
	@InsertProvider(method="insertSource",type=SourceSqlProvider.class)
	void insertSource(Source source);

	/**
	 * 更新内容源信息
	 * @param source
	 */
	@UpdateProvider(method="updateSource",type=SourceSqlProvider.class)
	void updateSource(Source source);

	/**
	 * 获取数据源抓取信息列表
	 * @param date
	 * @param status
	 * @return
	 */
	@SelectProvider(method="getSourceMonitorList",type=SourceSqlProvider.class)
	List<SourceCrawl> getSourceMonitorList(@Param(value = "date") Integer date, @Param(value = "status") Integer status);
	/**
	 * 获取内容源编号列表
	 * @param status
	 * @return
	 */
	@Select(value="select id from source where status = #{status}")
	List<Integer> getSourceMonitorIds(Integer status);
	/**
	 * 插入内容源监控记录
	 * @param ids
	 * @param today
	 */
	@InsertProvider(method="insertSourceMonitors",type=SourceSqlProvider.class)
	void insertSourceMonitors(@Param(value = "today")Integer today, @Param(value = "ids")List<Integer> ids);
	
	@Update(value="update source_monitor set update_time=now(),verify_rate = #{rate} where source_id = #{sourceId} and day = #{day}")
	void updateSourceByVerify(@Param(value = "sourceId")Integer sourceId,@Param(value = "day")Integer day,@Param(value = "rate")Double rate);

	@Select(value="select count(1) from source where url = #{url} or url = #{urlSuffix} ")
	int getSourceUrlCount(@Param(value = "url")String url, @Param(value = "urlSuffix")String urlSuffix);

	/**
	 * 获取内容源分类审核质量统计总数
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getQualityListCount", type = SourceSqlProvider.class)
    int getQualityListCount(QueryParam queryParam);

	/**
	 * 获取内容源分类审核质量统计记录
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getQualityList", type = SourceSqlProvider.class)
	List<SourceCrawl> getQualityList(QueryParam queryParam);

	/**
	 * 获取内容源抓取统计记录总数
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceCrawlListCount", type = SourceSqlProvider.class)
    int getSourceCrawlListCount(QueryParam queryParam);
	/**
	 * 获取内容源抓取统计记录
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceCrawlList", type = SourceSqlProvider.class)
	List<SourceCrawl> getSourceCrawlList(QueryParam queryParam);

	/**
	 * 获取内容源抓取总统计记录总数
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceCrawlTotalListCount", type = SourceSqlProvider.class)
	int getSourceCrawlTotalListCount(QueryParam queryParam);

	/**
	 * 获取内容源抓取统计总记录
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceCrawlTotalList", type = SourceSqlProvider.class)
	List<SourceCrawl> getSourceCrawlTotalList(QueryParam queryParam);

    /**
     * 获取内容源审核质量统计总数
     * @param queryParam
     * @return
     */
    @SelectProvider(method = "getQualityListTotalCount", type = SourceSqlProvider.class)
    int getQualityListTotalCount(QueryParam queryParam);

    /**
     * 获取内容源审核质量统计记录
     * @param queryParam
     * @return
     */
    @SelectProvider(method = "getQualityTotalList", type = SourceSqlProvider.class)
    List<SourceCrawl> getQualityTotalList(QueryParam queryParam);

    /**
     * 写入前一天数据源抓取统计
     * @param day
     * @param dayStr
     * @return
     */
    @Insert("insert into `stat_source_crawl_day`(source_id,source_type,grab_total,day,status)  select j.`source_id`,j.type,count(j.id),#{day},1  from joke j where j.create_time >= '${dayStr} 00:00:00' and j.create_time <= '${dayStr} 23:59:59' and j.`source_id` > 0 group by j.`source_id`,j.type")
    int insertSourceCrawlStat(@Param("day") String day, @Param("dayStr")String dayStr);

    /**
     * 获取数据源抓取记录列表
     * @param day
     * @return
     */
    @Select("select source_id as sourceId, grab_count as grabCount, day, last_grab_time as lastGrabTime from source_monitor where day = #{day}")
    List<SourceCrawl> getSourceMonitorCrawlList(@Param("day") String day);

    /**
     * 更新数据源的最后抓取时间与抓取次数
     * @param sourceCrawl
     */
    @Update("update stat_source_crawl_day set last_grab_time = #{lastGrabTime}, grab_count = #{grabCount}  where source_id = #{sourceId} and `day` = #{day}")
    void updateSourceCrawlLastGrabTimeAndGrabCount(SourceCrawl sourceCrawl);

    /**
     * 写入前一天数据源审核统计
     * @param day
     * @param dayStr
     * @return
     */
    @Insert("insert into `stat_quality_day`(source_id, source_type, passed, failed, day) select j.`source_id`, j.type, sum(case j.status when 3 then 1 when 1 then 1 else 0 end) as passed, sum(case j.status when 2 then 1 else 0 end) as failed, #{day} from joke j where j.verify_time >= '${dayStr} 00:00:00' and j.verify_time <= '${dayStr} 23:59:59' and j.`source_id` > 0   group by j.`source_id`,j.type ")
    int insertSourceQualityStat(@Param("day") String day, @Param("dayStr")String dayStr);

    /**
     * 更新数据源审核质量的最后抓取时间
     * @param sourceCrawl
     */
    @Update("update `stat_quality_day` set last_crawl_time = #{lastGrabTime} where source_id = #{sourceId} and `day` = #{day}")
    void updateSourceQualityLastGrabTime(SourceCrawl sourceCrawl);

	/**
	 * 获取数据源分类抓取统计导出列表
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceCrawlExport", type = SourceSqlProvider.class)
    List<SourceCrawlExport> getSourceCrawlExport(QueryParam queryParam);

	/**
	 * 获取数据源抓取统计导出列表
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceCrawlTotalExport", type = SourceSqlProvider.class)
	List<SourceCrawlExport> getSourceCrawlTotalExport(QueryParam queryParam);

	/**
	 * 获取数据源分类审核质量统计导出列表
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceQualityExport", type = SourceSqlProvider.class)
	List<SourceQualityExport> getSourceQualityExport(QueryParam queryParam);

	/**
	 * 获取数据源审核质量统计导出列表
	 * @param queryParam
	 * @return
	 */
	@SelectProvider(method = "getSourceQualityTotalExport", type = SourceSqlProvider.class)
	List<SourceQualityExport> getSourceQualityTotalExport(QueryParam queryParam);
}
