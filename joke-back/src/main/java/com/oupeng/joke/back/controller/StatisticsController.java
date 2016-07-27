package com.oupeng.joke.back.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.StatisticsService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.StatisExportUtil;
import com.oupeng.joke.domain.statistics.TimeDetail;
import com.oupeng.joke.domain.statistics.TimeDetailExport;
import com.oupeng.joke.domain.statistics.TimeTotal;

@Controller
@RequestMapping(value="statistics")
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private ChannelService channelService;
	
	@RequestMapping(value = "/dayTotal")
    public String showDayTotalList( @RequestParam(value="startDay",required=false)Integer startDay,
    		@RequestParam(value="endDay",required=false)Integer endDay,
    		@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
    		ModelMap model) {
    	pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<TimeTotal> list = null;
    	int count = statisticsService.getDayTotalCount(startDay, endDay);//总条数
    	if(count > 0){
    		if (count % pageSize == 0) {
				pageCount = count / pageSize;
			} else {
				pageCount = count / pageSize + 1;
			}
    		
			if (pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			offset = (pageNumber - 1) * pageSize;
			
			list = statisticsService.getDayTotalList(startDay, endDay, offset, pageSize);
    	}
    	model.addAttribute("startDay", startDay);
    	model.addAttribute("endDay", endDay);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	return "/statistics/dayTotal";
    }
	
	@RequestMapping(value = "/weekTotal")
    public String showWeekTotalList( @RequestParam(value="startWeek",required=false)Integer startWeek,
    		@RequestParam(value="endWeek",required=false)Integer endWeek,
    		@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
    		ModelMap model) {
    	pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<TimeTotal> list = null;
    	int count = statisticsService.getWeekTotalCount(startWeek, endWeek);//总条数
    	if(count > 0){
    		if (count % pageSize == 0) {
				pageCount = count / pageSize;
			} else {
				pageCount = count / pageSize + 1;
			}
    		
			if (pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			offset = (pageNumber - 1) * pageSize;
			
			list = statisticsService.getWeekTotalList(startWeek, endWeek, offset, pageSize);
    	}
    	model.addAttribute("startWeek", startWeek);
    	model.addAttribute("endWeek", endWeek);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	return "/statistics/weekTotal";
    }
	
	@RequestMapping(value = "/monthTotal")
    public String showMonthTotalList( @RequestParam(value="startMonth",required=false)Integer startMonth,
    		@RequestParam(value="endMonth",required=false)Integer endMonth,
    		@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
    		ModelMap model) {
    	pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<TimeTotal> list = null;
    	int count = statisticsService.getMonthTotalCount(startMonth, endMonth);//总条数
    	if(count > 0){
    		if (count % pageSize == 0) {
				pageCount = count / pageSize;
			} else {
				pageCount = count / pageSize + 1;
			}
    		
			if (pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			offset = (pageNumber - 1) * pageSize;
			
			list = statisticsService.getMonthTotalList(startMonth, endMonth, offset, pageSize);
    	}
    	model.addAttribute("startMonth", startMonth);
    	model.addAttribute("endMonth", endMonth);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	return "/statistics/monthTotal";
    }
	
	@RequestMapping(value = "/dayDetail")
    public String showDayDetailList( @RequestParam(value="startDay",required=false)Integer startDay,
    		@RequestParam(value="endDay",required=false)Integer endDay,
    		@RequestParam(value="dname",required=false)String distributorName,
    		@RequestParam(value="cname",required=false)String channelName,
    		@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
    		@RequestParam(value="type",required=false,defaultValue="0")Integer type,
    		ModelMap model) throws IOException {
    	pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<TimeDetail> list = null;
    	String dids = null;
    	String cids = null;
    	String lids = null;
    	boolean flag = true;
    	if(StringUtils.isNotBlank(distributorName)){
    		dids = distributorService.getDistributorIdListByName(distributorName);
    		if(StringUtils.isBlank(dids)){
    			flag = false;
    		}
    	}
    	
    	if(flag && StringUtils.isNotBlank(channelName)){
    		cids = channelService.getChannelIdListByName(channelName);
    		if(StringUtils.isBlank(cids)){
    			flag = false;
    		}
    	}
    	
    	int count = 0;
    	if(flag){
    		count = statisticsService.getDayDetailCount(startDay, endDay,dids,lids,type);//总条数
    	}
    	if(count > 0){
    		if (count % pageSize == 0) {
				pageCount = count / pageSize;
			} else {
				pageCount = count / pageSize + 1;
			}
    		
			if (pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			offset = (pageNumber - 1) * pageSize;
			
			list = statisticsService.getDayDetailList(startDay, endDay,dids,cids,type, offset, pageSize);
    	}
    	model.addAttribute("startDay", startDay);
    	model.addAttribute("endDay", endDay);
    	model.addAttribute("distributorName", distributorName);
    	model.addAttribute("channelName", channelName);
    	model.addAttribute("type", type);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	model.addAttribute("distributorList", distributorService.getAllDistributorList());
    	model.addAttribute("channelList", channelService.getChannelList(null));
    	return "/statistics/dayDetail";
    }
	
	@RequestMapping(value = "/dayDetailExport")
    public void exportDayDetailList( @RequestParam(value="startDay",required=false)Integer startDay,
    		@RequestParam(value="endDay",required=false)Integer endDay,
    		@RequestParam(value="dname",required=false)String distributorName,
    		@RequestParam(value="cname",required=false)String channelName,
    		@RequestParam(value="type",required=false,defaultValue="0")Integer type,
    		HttpServletResponse response) throws IOException {
		String dids = null;
    	String cids = null;
    	boolean flag = true;
    	if(StringUtils.isNotBlank(distributorName)){
    		dids = distributorService.getDistributorIdListByName(distributorName);
    		if(StringUtils.isBlank(dids)){
    			flag = false;
    		}
    	}
    	
    	if(flag && StringUtils.isNotBlank(channelName)){
    		cids = channelService.getChannelIdListByName(channelName);
    		if(StringUtils.isBlank(cids)){
    			flag = false;
    		}
    	}
    	
    	int count = 0;
    	if(flag){
    		count = statisticsService.getDayDetailCount(startDay, endDay,dids,cids,type);//总条数
    	}
    	if(count > 0){
			List<TimeDetailExport> exportList = statisticsService.getDayDetailList4Export(startDay, endDay,dids,cids,type);
			new StatisExportUtil<TimeDetailExport>().exportExcel("段子渠道日报", Constants.STATIS_DAY_DETAIL, exportList, response);
    	}
    }
	
	@RequestMapping(value = "/weekDetail")
    public String showWeekDetailList( @RequestParam(value="startWeek",required=false)Integer startWeek,
    		@RequestParam(value="endWeek",required=false)Integer endWeek,
    		@RequestParam(value="dname",required=false)String distributorName,
    		@RequestParam(value="cname",required=false)String channelName,
    		@RequestParam(value="type",required=false,defaultValue="0")Integer type,
    		@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
    		ModelMap model) {
    	pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<TimeDetail> list = null;
    	String dids = null;
    	String cids = null;
    	boolean flag = true;
    	if(StringUtils.isNotBlank(distributorName)){
    		dids = distributorService.getDistributorIdListByName(distributorName);
    		if(StringUtils.isBlank(dids)){
    			flag = false;
    		}
    	}
    	
    	if(flag && StringUtils.isNotBlank(channelName)){
    		cids = channelService.getChannelIdListByName(channelName);
    		if(StringUtils.isBlank(cids)){
    			flag = false;
    		}
    	}
    	
    	int count = 0;
    	if(flag){
    		count = statisticsService.getWeekDetailCount(startWeek, endWeek,dids,cids,type);//总条数
    	}
    	if(count > 0){
    		if (count % pageSize == 0) {
				pageCount = count / pageSize;
			} else {
				pageCount = count / pageSize + 1;
			}
    		
			if (pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			offset = (pageNumber - 1) * pageSize;
			
			list = statisticsService.getWeekDetailList(startWeek, endWeek,dids,cids,type, offset, pageSize);
    	}
    	model.addAttribute("startWeek", startWeek);
    	model.addAttribute("endWeek", endWeek);
    	model.addAttribute("distributorName", distributorName);
    	model.addAttribute("channelName", channelName);
    	model.addAttribute("type", type);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	model.addAttribute("distributorList", distributorService.getAllDistributorList());
    	model.addAttribute("channelList", channelService.getChannelList(null));
    	return "/statistics/weekDetail";
    }
	
	@RequestMapping(value = "/monthDetail")
    public String showMonthDetailList( @RequestParam(value="startMonth",required=false)Integer startMonth,
    		@RequestParam(value="endMonth",required=false)Integer endMonth,
    		@RequestParam(value="dname",required=false)String distributorName,
    		@RequestParam(value="cname",required=false)String channelName,
    		@RequestParam(value="type",required=false,defaultValue="0")Integer type,
    		@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
    		ModelMap model) {
    	pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<TimeDetail> list = null;
    	String dids = null;
    	String cids = null;
    	boolean flag = true;
    	if(StringUtils.isNotBlank(distributorName)){
    		dids = distributorService.getDistributorIdListByName(distributorName);
    		if(StringUtils.isBlank(dids)){
    			flag = false;
    		}
    	}
    	
    	if(flag && StringUtils.isNotBlank(channelName)){
    		cids = channelService.getChannelIdListByName(channelName);
    		if(StringUtils.isBlank(cids)){
    			flag = false;
    		}
    	}
    	
    	int count = 0;
    	if(flag){
    		count = statisticsService.getMonthDetailCount(startMonth, endMonth,dids,cids,type);//总条数
    	}
    	if(count > 0){
    		if (count % pageSize == 0) {
				pageCount = count / pageSize;
			} else {
				pageCount = count / pageSize + 1;
			}
    		
			if (pageNumber > pageCount) {
				pageNumber = pageCount;
			}
			if (pageNumber < 1) {
				pageNumber = 1;
			}
			offset = (pageNumber - 1) * pageSize;
			
			list = statisticsService.getMonthDetailList(startMonth, endMonth,dids,cids,type, offset, pageSize);
    	}
    	model.addAttribute("startMonth", startMonth);
    	model.addAttribute("endMonth", endMonth);
    	model.addAttribute("distributorName", distributorName);
    	model.addAttribute("channelName", channelName);
    	model.addAttribute("type", type);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	model.addAttribute("distributorList", distributorService.getAllDistributorList());
    	model.addAttribute("channelList", channelService.getChannelList(null));
    	return "/statistics/monthDetail";
    }
}
