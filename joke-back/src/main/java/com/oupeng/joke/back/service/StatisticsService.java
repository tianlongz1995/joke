package com.oupeng.joke.back.service;

import java.util.List;

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
}