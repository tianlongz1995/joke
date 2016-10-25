package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.StatisticsService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.back.util.StatisExportUtil;
import com.oupeng.joke.domain.statistics.DropDetail;
import com.oupeng.joke.domain.statistics.TimeDetail;
import com.oupeng.joke.domain.statistics.TimeDetailExport;
import com.oupeng.joke.domain.statistics.TimeTotal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value="statistics")
public class StatisticsController {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
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

	/**
	 * 刷新统计
	 * @param startDay		开始日期
	 * @param endDay		结束日期
	 * @param flushType		刷新类型 0:下拉刷新、1:上拉刷新
	 * @param dateType		日期类型 0：日报；1：周报；2：月报
	 * @param pageNumber	页码
	 * @param pageSize		记录数
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dropTotal")
	public String dropTotal( @RequestParam(value="startDay",required=false)Integer startDay,
									@RequestParam(value="endDay",required=false)Integer endDay,
							 		@RequestParam(value="flushType",required=false,defaultValue="0")Integer flushType,
							 		@RequestParam(value="dateType",required=false,defaultValue="0")Integer dateType,
									@RequestParam(value="pageNumber",required=false)Integer pageNumber,
									@RequestParam(value="pageSize",required=false)Integer pageSize,
									ModelMap model) {
		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset = 0 ;//开始条数index
		List<TimeTotal> list = null;
		int count = statisticsService.getDayDropTotalCount(startDay, endDay, flushType, dateType);//总条数
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

			list = statisticsService.getDayDropTotalList(startDay, endDay, offset, pageSize, flushType, dateType);
		}
		model.addAttribute("startDay", startDay);
		model.addAttribute("endDay", endDay);
		model.addAttribute("count", count);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("flushType", flushType);
		model.addAttribute("dateType", dateType);
		model.addAttribute("list", list);
		return "/statistics/dropTotal";
	}

	/**
	 * 刷新日报明细
	 * @param startDay		开始日期
	 * @param endDay		结束日期
	 * @param flushType		刷新类型 0:下拉刷新、1:上拉刷新
	 * @param dateType		日期类型 0：日报；1：周报；2：月报
	 * @param pageNumber	页码
	 * @param pageSize		记录数
	 * @param distributorName	渠道名称
	 * @param channelName		频道名称
	 * @param type			类型:0:渠道;1:频道;2:标签
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/dropDetail")
	public String dropDetail( @RequestParam(value="startDay",required=false)Integer startDay,
									 @RequestParam(value="endDay",required=false)Integer endDay,
							  		 @RequestParam(value="flushType",required=false,defaultValue="0")Integer flushType,
							  		 @RequestParam(value="dateType",required=false,defaultValue="0")Integer dateType,
									 @RequestParam(value="dname",required=false)String distributorName,
									 @RequestParam(value="cname",required=false)String channelName,
									 @RequestParam(value="pageNumber",required=false)Integer pageNumber,
									 @RequestParam(value="pageSize",required=false)Integer pageSize,
									 @RequestParam(value="type",required=false,defaultValue="0")Integer type,
									 ModelMap model) throws IOException {

		try {
			pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
			pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
			int pageCount = 0;//总页数
			int offset = 0;//开始条数index
			List<DropDetail> list = null;
			String dids = null;
			String cids = null;
			String lids = null;
			boolean flag = true;
			if (StringUtils.isNotBlank(distributorName)) {
				dids = distributorService.getDistributorIdListByName(distributorName);
				if (StringUtils.isBlank(dids)) {
					flag = false;
				}
			}

			if (flag && StringUtils.isNotBlank(channelName)) {
				cids = channelService.getChannelIdListByName(channelName);
				if (StringUtils.isBlank(cids)) {
					flag = false;
				}
			}
			int count = 0;
			if (flag) {
				count = statisticsService.getDropDayDetailCount(startDay, endDay, dids, lids, type, flushType, dateType);//总条数
			}
			if (count > 0) {
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

				list = statisticsService.getDropDayDetailList(startDay, endDay, dids, cids, type, offset, pageSize, flushType, dateType);
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
			model.addAttribute("flushType", flushType);
			model.addAttribute("dateType", dateType);
			model.addAttribute("list", list);
			return "/statistics/dropDetail";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "/statistics/dropDetail";
	}


	/**
	 * 使用统计
	 * @param startDay		开始时间
	 * @param endDay		结束时间
	 * @param reportType	报告类型: 0:总报; 1:渠道; 2:频道; 3:明细
	 * @param dateType		日期类型: 0:日报; 1:周报; 2:月报
	 * @param logType		日志类型 0：长图展开点击日志；1：上拉刷新日志；2：下拉刷新日志
	 * @param distributorId
	 * @param channelId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/usage")
	public String usage(@RequestParam(value="startDay",required=false)Integer startDay,
						@RequestParam(value="endDay",required=false)Integer endDay,
						@RequestParam(value="reportType",required=false,defaultValue="0")Integer reportType,
						@RequestParam(value="dateType",required=false,defaultValue="0")Integer dateType,
						@RequestParam(value="logType",required=false,defaultValue="0")Integer logType,
						@RequestParam(value="did",required=false)Integer distributorId,
						@RequestParam(value="cid",required=false)Integer channelId,
						@RequestParam(value="pageNumber",required=false)Integer pageNumber,
						@RequestParam(value="pageSize",required=false)Integer pageSize,
						ModelMap model) throws IOException {

		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset = 0 ;//开始条数index
		List<DropDetail> list = null;
		if(startDay == null || endDay == null){
			startDay = FormatUtil.getYesterday();
			endDay = startDay;
		}

		int count = statisticsService.getUsageCount(startDay, endDay, reportType, dateType, logType, distributorId, channelId);//总条数
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

			list = statisticsService.getUsageList(startDay, endDay, offset, pageSize, reportType, dateType, logType, distributorId, channelId);
		}
		model.addAttribute("startDay", startDay);
		model.addAttribute("endDay", endDay);
		model.addAttribute("count", count);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("reportType", reportType);
		model.addAttribute("dateType", dateType);
		model.addAttribute("logType", logType);
		model.addAttribute("distributorId", distributorId);
		model.addAttribute("channelId", channelId);
		model.addAttribute("list", list);
		model.addAttribute("distributorList", distributorService.getAllDistributorList());
		model.addAttribute("channelList", channelService.getChannelList(Constants.CHANNEL_STATUS_VALID));
		return "/statistics/usage";
	}

}
