package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.SourceSqlProvider;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.Source;
import com.oupeng.joke.domain.SourceCrawl;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SourceMapper {
	/**
	 * 获取广告列表总数
	 * @param source
	 * @return
	 */
	@SelectProvider(method="getSourceListCount",type=SourceSqlProvider.class)
	int getSourceListCount(Source source);
	/**
	 * 获取广告列表
	 * @param source        广告
	 * @return
	 */
	@SelectProvider(method="getSourceList",type=SourceSqlProvider.class)
	List<Source> getSourceList(Source source);

	/**
	 * 获取广告信息
	 * @param id 广告编号
	 * @return
	 */
	@Select(value="select id, name, url, status,create_time as createTime,update_time as updateTime from source where id = ${id}")
	Source getSourceById(@Param(value = "id") Integer id);

	/**
	 * 删除（逻辑删除）
	 * @param id
	 */
	@Select(value="update source set update_time=now(),status = 2 where id = ${id}")
	void del(@Param(value = "id") Integer id);


	/**
	 * 新增广告
	 * @param source
	 */
	@InsertProvider(method="insertSource",type=SourceSqlProvider.class)
	void insertSource(Source source);

	/**
	 * 更新广告信息
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

}
