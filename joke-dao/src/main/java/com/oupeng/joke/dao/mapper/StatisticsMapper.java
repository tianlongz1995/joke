package com.oupeng.joke.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import com.oupeng.joke.dao.sqlprovider.StatisticsSqlProvider;
import com.oupeng.joke.domain.statistics.TimeDetail;
import com.oupeng.joke.domain.statistics.TimeTotal;
import com.oupeng.joke.domain.statistics.DropDetail;

public interface StatisticsMapper {

	@SelectProvider(method="getDayTotalCount",type=StatisticsSqlProvider.class)
	Integer getDayTotalCount(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay);
	
	@SelectProvider(method="getDayTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	List<TimeTotal> getDayTotalList(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getWeekTotalCount",type=StatisticsSqlProvider.class)
	Integer getWeekTotalCount(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek);
	
	@SelectProvider(method="getWeekTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	List<TimeTotal> getWeekTotalList(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);	
	
	@SelectProvider(method="getMonthTotalCount",type=StatisticsSqlProvider.class)
	Integer getMonthTotalCount(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth);
	
	@SelectProvider(method="getMonthTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	List<TimeTotal> getMonthTotalList(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getDayDetailCount",type=StatisticsSqlProvider.class)
	Integer getDayDetailCount(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);
	
	@SelectProvider(method="getDayDetailList",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	List<TimeDetail> getDayDetailList(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getDayDetailList4Export",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	List<TimeDetail> getDayDetailList4Export(@Param(value = "startDay")Integer startDay,@Param(value = "endDay")Integer endDay,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);

	@SelectProvider(method="getWeekDetailCount",type=StatisticsSqlProvider.class)
	Integer getWeekDetailCount(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);
	
	@SelectProvider(method="getWeekDetailList",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	List<TimeDetail> getWeekDetailList(@Param(value = "startWeek")Integer startWeek,@Param(value = "endWeek")Integer endWeek,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);
	
	@SelectProvider(method="getMonthDetailCount",type=StatisticsSqlProvider.class)
	Integer getMonthDetailCount(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type);
	
	@SelectProvider(method="getMonthDetailList",type=StatisticsSqlProvider.class)
	@ResultType(TimeDetail.class)
	List<TimeDetail> getMonthDetailList(@Param(value = "startMonth")Integer startMonth,@Param(value = "endMonth")Integer endMonth,
			@Param(value = "dids")String dids,@Param(value = "cids")String cids,@Param(value = "type")Integer type,
			@Param(value = "start")Integer start,@Param(value = "end")Integer end);

	/**
	 * 获取下拉刷新日报表总数
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@SelectProvider(method="getDayDropTotalCount",type=StatisticsSqlProvider.class)
	Integer getDayDropTotalCount(@Param("startDay")Integer startDay,
								 @Param("endDay")Integer endDay,
								 @Param("flushType")Integer flushType,
								 @Param("dateType")Integer dateType);

	/**
	 * 获取下拉刷新日报表记录
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@SelectProvider(method="getDayDropTotalList",type=StatisticsSqlProvider.class)
	@ResultType(TimeTotal.class)
	List<TimeTotal> getDayDropTotalList(@Param("startDay")Integer startDay,
										@Param("endDay")Integer endDay,
										@Param("start")Integer start,
										@Param("end")Integer end,
										@Param("flushType")Integer flushType,
										@Param("dateType")Integer dateType);

	/**
	 * 获取下拉刷新日报明细总数
	 * @param startDay
	 * @param endDay
	 * @param dids
	 * @param cids
	 * @param type
	 * @return
	 */
	@SelectProvider(method="getDropDayDetailCount",type=StatisticsSqlProvider.class)
	Integer getDropDayDetailCount(@Param("startDay")Integer startDay,
								  @Param("endDay")Integer endDay,
								  @Param("dids")String dids,
								  @Param("cids")String cids,
								  @Param("type")Integer type,
								  @Param("flushType")Integer flushType,
								  @Param("dateType")Integer dateType);

	/**
	 * 获取下拉刷新日报明细记录
	 * @param startDay
	 * @param endDay
	 * @param dids
	 * @param cids
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	@SelectProvider(method="getDropDayDetailList",type=StatisticsSqlProvider.class)
	@ResultType(DropDetail.class)
	List<DropDetail> getDropDayDetailList(@Param("startDay")Integer startDay,
										  @Param("endDay")Integer endDay,
										  @Param("dids")String dids,
										  @Param("cids")String cids,
										  @Param("type")Integer type,
										  @Param("start")Integer start,
										  @Param("end")Integer end,
										  @Param("flushType")Integer flushType,
										  @Param("dateType")Integer dateType);

	/**
	 * 获取图片展开点击统计总记录数
	 * @param startDay
	 * @param endDay
	 * @param type
	 * @param dateType
	 * @param distributorId
	 * @param channelId
	 * @return
	 */
	@SelectProvider(method="getImageOpenCount",type=StatisticsSqlProvider.class)
	int getImageOpenCount(@Param("startDay") Integer startDay,
						  @Param("endDay") Integer endDay,
						  @Param("reportType") Integer type,
						  @Param("dateType") Integer dateType,
						  @Param("distributorId") Integer distributorId,
						  @Param("channelId") Integer channelId);

	/**
	 * 获取图片展开点击统计记录列表
	 * @param startDay
	 * @param endDay
	 * @param offset
	 * @param pageSize
	 * @param type
	 * @param dateType
	 * @param distributorId
	 * @param channelId
	 * @return
	 */
	@SelectProvider(method="getImageOpenList",type=StatisticsSqlProvider.class)
	List<DropDetail> getImageOpenList(@Param("startDay") Integer startDay,
									 @Param("endDay") Integer endDay,
									 @Param("offset") Integer offset,
									 @Param("pageSize") Integer pageSize,
									 @Param("reportType") Integer type,
									 @Param("dateType") Integer dateType,
									 @Param("distributorId") Integer distributorId,
									 @Param("channelId") Integer channelId);
}