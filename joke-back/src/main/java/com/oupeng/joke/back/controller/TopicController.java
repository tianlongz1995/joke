package com.oupeng.joke.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;

@Controller
@RequestMapping(value="/topic")
public class TopicController {

	@Autowired
	private TopicService topicService;
	@Autowired
	private DistributorService distributorService;
	
	@RequestMapping(value="/list")
	public String getTopicList(@RequestParam(value="status",required=false)Integer status,Model model){
		model.addAttribute("list", topicService.getTopicList(status));
		model.addAttribute("distributorList", distributorService.getDistributorList(Constants.DISTRIBUTOR_STATUS_VALID));
		model.addAttribute("status", status);
		return "/topic/list";
	} 
	
	@RequestMapping(value="/verify")
	@ResponseBody
	public Result verify(@RequestParam(value="id")Integer id,
			@RequestParam(value="status")Integer status){
		String result = topicService.updateTopicStatus(id, status);
		if(result == null){
			return new Success();
		}else{
			return new Failed(result);
		}
	} 
	

	@RequestMapping(value="/edit")
	public String edit(@RequestParam(value="id")Integer id,Model model){
		model.addAttribute("topic", topicService.getTopicById(id));
		model.addAttribute("distributorList", distributorService.getDistributorList(Constants.DISTRIBUTOR_STATUS_VALID));
		return "/topic/edit";
	} 
	
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id",required=true)Integer id,
			@RequestParam(value="title",required=false)String title,
			@RequestParam(value="img",required=false)String img,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="dids",required=false)String dids,
			@RequestParam(value="publishTime",required=false)String publishTime){
		topicService.updateTopic(id, title, img, content, dids, publishTime);
		return new Success();
	} 
	
	@RequestMapping(value="/add")
	@ResponseBody
	public Result add(@RequestParam(value="title",required=false)String title,
			@RequestParam(value="img",required=false)String img,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="dids",required=false)String dids,
			@RequestParam(value="publishTime",required=false)String publishTime){
		topicService.insertTopic(title, img, content, dids, publishTime);;
		return new Success();
	} 
}
