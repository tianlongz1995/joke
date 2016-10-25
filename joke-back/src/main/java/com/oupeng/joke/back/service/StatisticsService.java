package com.oupeng.joke.back.service;

import java.util.List;

import com.oupeng.joke.domain.statistics.DropDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.oupeng.joke.back.util.TransformUtil;
import com.oupeng.joke.dao.mapper.StatisticsMapper;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.statistics.TimeDetail;
import com.oupeng.joke.domain.statistics.TimeDetailExport;
import com.oupeng.joke.domain.statistics.TimeTotal;

@Service
public class StatisticsService {

	@Autowired
	private StatisticsMapper statisticsMapper;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private ChannelService channelService;
	
	public Integer getDayTotalCount(Integer startDay,Integer endDay){
		return statisticsMapper.getDayTotalCount(startDay, endDay);
	}
	
	public List<TimeTotal> getDayTotalList(Integer startDay,Integer endDay,Integer start,Integer end){
		return statisticsMapper.getDayTotalList(startDay, endDay,start,end);
	}
	
	public Integer getWeekTotalCount(Integer startWeek,Integer endWeek){
		return statisticsMapper.getWeekTotalCount(startWeek, endWeek);
	}
	
	public List<TimeTotal> getWeekTotalList(Integer startWeek,Integer endWeek,Integer start,Integer end){
		return statisticsMapper.getWeekTotalList(startWeek, endWeek,start,end);
	}
	
	public Integer getMonthTotalCount(Integer startMonth,Integer endMonth){
		return statisticsMapper.getMonthTotalCount(startMonth, endMonth);
	}
	
	public List<TimeTotal> getMonthTotalList(Integer startMonth,Integer endMonth,Integer start,Integer end){
		return statisticsMapper.getMonthTotalList(startMonth, endMonth,start,end);
	}
	
	public Integer getDayDetailCount(Integer startDay,Integer endDay,String distributorIds,String channelIds,Integer type){
		return statisticsMapper.getDayDetailCount(startDay, endDay,distributorIds,channelIds,type);
	}
	
	public List<TimeDetail> getDayDetailList(Integer startDay,Integer endDay,String distributorIds,String channelIds,Integer type,Integer start,Integer end){
		return statisticsMapper.getDayDetailList(startDay, endDay,distributorIds,channelIds,type,start,end);
	}
	
	public List<TimeDetailExport> getDayDetailList4Export(Integer startDay,Integer endDay,String distributorIds,String channelIds,Integer type){
		List<TimeDetailExport> result = null;
		List<TimeDetail> list = statisticsMapper.getDayDetailList4Export(startDay, endDay,distributorIds,channelIds,type);
		if(!CollectionUtils.isEmpty(list)){
			List<Distributor> distributorList = distributorService.getAllDistributorList();
			List<Channel> channelList = channelService.getChannelList(null);
			result = Lists.newArrayList();
			TimeDetailExport export = null;
			for(TimeDetail timeDetail : list){
				export = new TimeDetailExport();
				TransformUtil.exec(timeDetail, export);
				TransformUtil.exec(export, distributorList, channelList);
				result.add(export);
			}
		}
		return result;
	}
	
	public Integer getWeekDetailCount(Integer startWeek,Integer endWeek,String distributorIds,String channelIds,Integer type){
		return statisticsMapper.getWeekDetailCount(startWeek, endWeek,distributorIds,channelIds,type);
	}
	
	public List<TimeDetail> getWeekDetailList(Integer startWeek,Integer endWeek,String distributorIds,String channelIds,Integer type,Integer start,Integer end){
		return statisticsMapper.getWeekDetailList(startWeek, endWeek,distributorIds,channelIds,type,start,end);
	}
	
	public Integer getMonthDetailCount(Integer startMonth,Integer endMonth,String distributorIds,String channelIds,Integer type){
		return statisticsMapper.getMonthDetailCount(startMonth, endMonth,distributorIds,channelIds,type);
	}
	
	public List<TimeDetail> getMonthDetailList(Integer startMonth,Integer endMonth,String distributorIds,String channelIds,Integer type,Integer start,Integer end){
		return statisticsMapper.getMonthDetailList(startMonth, endMonth,distributorIds,channelIds,type,start,end);
	}

	/**
	 * 获取下拉刷新日报表总数
     * @param startDay    开始日期
     * @param endDay    结束日期
	 * @param flushType		刷新类型 0:下拉刷新、1:上拉刷新
	 * @param dateType		日期类型 0：日报；1：周报；2：月报
	 * @return
	 */
	public Integer getDayDropTotalCount(Integer startDay, Integer endDay, Integer flushType, Integer dateType){
		return statisticsMapper.getDayDropTotalCount(startDay, endDay, flushType, dateType);
	}

	/**
	 * 获取下拉刷新日报表记录
	 * @param startDay
	 * @param endDay
	 * @param start
	 * @param end
	 * @param flushType		刷新类型 0:下拉刷新、1:上拉刷新
	 * @param dateType		日期类型 0：日报；1：周报；2：月报
	 * @return
	 */
	public List<TimeTotal> getDayDropTotalList(Integer startDay,Integer endDay,Integer start,Integer end, Integer flushType, Integer dateType){
		return statisticsMapper.getDayDropTotalList(startDay, endDay, start, end, flushType, dateType);
	}

	/**
	 * 获取下拉刷新日报明细总数
	 * @param startDay
	 * @param endDay
	 * @param distributorIds
	 * @param channelIds
	 * @param type
	 * @param flushType		刷新类型 0:下拉刷新、1:上拉刷新
	 * @param dateType		日期类型 0：日报；1：周报；2：月报
	 * @return
	 */
	public Integer getDropDayDetailCount(Integer startDay,Integer endDay,String distributorIds,String channelIds,Integer type, Integer flushType, Integer dateType){
		return statisticsMapper.getDropDayDetailCount(startDay, endDay, distributorIds, channelIds, type, flushType, dateType);
	}

	/**
	 * 获取下拉刷新日报明细记录
	 * @param startDay
	 * @param endDay
	 * @param distributorIds
	 * @param channelIds
	 * @param type
	 * @param start
	 * @param end
	 * @param flushType		刷新类型 0:下拉刷新、1:上拉刷新
	 * @param dateType		日期类型 0：日报；1：周报；2：月报
	 * @return
	 */
	public List<DropDetail> getDropDayDetailList(Integer startDay, Integer endDay, String distributorIds, String channelIds, Integer type, Integer start, Integer end, Integer flushType, Integer dateType){
		return statisticsMapper.getDropDayDetailList(startDay, endDay, distributorIds, channelIds, type, start, end, flushType, dateType);
	}

	/**
	 * 获取图片展开点击统计总记录数
	 * @param startDay
	 * @param endDay
	 * @param reportType	报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
	 * @param dateType		日期类型: 0:日报; 1:周报; 2:月报
	 * @param logType		日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
	 * @return
	 */
    public int getUsageCount(Integer startDay, Integer endDay, Integer reportType, Integer dateType, Integer logType, Integer distributorId, Integer channelId) {
    	return statisticsMapper.getUsageCount(startDay, endDay, reportType, dateType, logType, distributorId, channelId);
    }

	/**
	 * 获取图片展开点击统计记录列表
	 * @param startDay
	 * @param endDay
	 * @param offset
	 * @param pageSize
	 * @param reportType	报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
	 * @param dateType		日期类型: 0:日报; 1:周报; 2:月报
	 * @param logType		日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
	 * @return
	 */
	public List<DropDetail> getUsageList(Integer startDay, Integer endDay, int offset, Integer pageSize, Integer reportType, Integer dateType, Integer logType, Integer distributorId, Integer channelId) {
		return statisticsMapper.getUsageList(startDay, endDay, offset, pageSize, reportType, dateType, logType, distributorId, channelId);
	}
}
