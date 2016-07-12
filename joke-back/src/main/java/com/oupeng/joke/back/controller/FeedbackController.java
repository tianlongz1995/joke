package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.FeedbackService;
import com.oupeng.joke.domain.PieGraph;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 反馈管理
 */
@Controller
@RequestMapping(value="/feedback")
public class FeedbackController {
	private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private DistributorService distributorService;
	private static String[] colors = {"#fedd74", "#82d8ef", "#f76864", "#80bd91", "#fd9fc1", "#a5c2d5", "#cbab4f", "#76a871", "#a56f8f"};

	/**
	 * 反馈列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/monitor")
	public String getFeedbackList(Model model) {
		model.addAttribute("dList", distributorService.getAllDistributorList());
		return "/feedback/monitor";
	}

	/**
	 * 查询渠道反馈数量列表
	 * @return
	 */
	@RequestMapping(value = "/pieGraph", produces = {"application/json"})
	@ResponseBody
	public Object getFeedbackCountList(@RequestParam(value = "startDate", required = false) String startDate,
									   @RequestParam(value = "endDate", required = false) String endDate,
									   @RequestParam(value = "distributorId", required = false) Integer distributorId,
									   Model model) {
//        默认查询当天记录
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(startDate)){
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			startDate = date;
			endDate = date;
		}
		List<PieGraph> list = feedbackService.getDistributorFeedbackCountList(startDate, endDate, distributorId);
		if(!list.isEmpty()){
			int index = 0;
			for (PieGraph pie : list) {
				pie.setColor(colors[index]);
				index++;
			}
		}
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("distributorId", distributorId);
		model.addAttribute("dList", distributorService.getAllDistributorList());
		return  list;
	}

	/**
	 * 查询渠道反馈类型列表
	 * @return
	 */
	@RequestMapping(value = "/lineGraph", produces = {"application/json"})
	@ResponseBody
	public Object getFeedbackTypeList(@RequestParam(value = "startDate", required = false) String startDate,
									  @RequestParam(value = "endDate", required = false) String endDate,
									  @RequestParam(value = "distributorId", required = false) Integer distributorId,
									  Model model) {
//        默认查询当天记录
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(startDate)){
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			startDate = date;
			endDate = date;
		}
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("distributorId", distributorId);
		model.addAttribute("dList", distributorService.getAllDistributorList());
		return feedbackService.getDistributorFeedbackTypeList(startDate, endDate, distributorId);
	}
}