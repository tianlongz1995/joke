package com.oupeng.joke.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import com.oupeng.joke.dao.sqlprovider.StatisticsSqlProvider;
import com.oupeng.joke.domain.statistics.TimeDetail;
import com.oupeng.joke.domain.statistics.TimeTotal;

public interface StatisticsMapper {

	@SelectProvider(method="getDayTotalCount",type=StatisticsSqlProvider.class)
	public Integer getDayTotalCount(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay);
	
	@SelectProvider(method="getDayTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	public List<TimeTotal> getDayTotalList(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getWeekTotalCount",type=StatisticsSqlProvider.class)
	public Integer getWeekTotalCount(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek);
	
	@SelectProvider(method="getWeekTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	public List<TimeTotal> getWeekTotalList(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);	
	
	@SelectProvider(method="getMonthTotalCount",type=StatisticsSqlProvider.class)
	public Integer getMonthTotalCount(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth);
	
	@SelectProvider(method="getMonthTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	public List<TimeTotal> getMonthTotalList(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getDayDetailCount",type=StatisticsSqlProvider.class)
	public Integer getDayDetailCount(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);
	
	@SelectProvider(method="getDayDetailList",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	public List<TimeDetail> getDayDetailList(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getDayDetailList4Export",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	public List<TimeDetail> getDayDetailList4Export(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);

	@SelectProvider(method="getWeekDetailCount",type=StatisticsSqlProvider.class)
	public Integer getWeekDetailCount(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);
	
	@SelectProvider(method="getWeekDetailList",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	public List<TimeDetail> getWeekDetailList(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getMonthDetailCount",type=StatisticsSqlProvider.class)
	public Integer getMonthDetailCount(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);
	
	@SelectProvider(method="getMonthDetailList",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	public List<TimeDetail> getMonthDetailList(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
}