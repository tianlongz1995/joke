package com.oupeng.joke.back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;

@Controller
@RequestMapping(value="/topic")
public class TopicController {
	private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
	@Autowired
	private TopicService topicService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private JokeService jokeService;
	
	
	@RequestMapping(value="/list")
	public String getTopicList(@RequestParam(value="status",required=false)Integer status,Model model){
		model.addAttribute("list", topicService.getTopicList(status));
		model.addAttribute("distributorList", distributorService.getAllDistributorList());
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
		model.addAttribute("distributorList", distributorService.getAllDistributorList());
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
	
	@RequestMapping(value="/addjoke")
	public String getJokeList4Add(@RequestParam(value="topicId",required=true)Integer topicId,
			@RequestParam(value="type",required=false)Integer type,
			Model model){
    	model.addAttribute("topicId", topicId);
    	model.addAttribute("type", type);
    	model.addAttribute("list", jokeService.getJokeListForTopic(type, Constants.JOKE_STATUS_VALID));
		return "/topic/addjoke";
	}
	
	@RequestMapping(value="/deljoke")
	public String getJokeList4Del(@RequestParam(value="topicId",required=true)Integer topicId,
			Model model){
    	model.addAttribute("topicId", topicId);
    	model.addAttribute("list", topicService.getJokeListByTopicId(topicId));
		return "/topic/deljoke";
	}
	
	@RequestMapping(value="/addBatchJoke")
	@ResponseBody
	public Result add(@RequestParam(value="ids",required=false)String ids,
			@RequestParam(value="topicId",required=true)Integer topicId){
		topicService.addTopicJoke(ids, topicId);
		return new Success();
	}
	
	@RequestMapping(value="/delBatchJoke")
	@ResponseBody
	public Result del(@RequestParam(value="ids",required=false)String ids,
			@RequestParam(value="topicId",required=true)Integer topicId){
		topicService.delTopicJoke(ids, topicId);
		return new Success();
	}

	/**
	 * 添加原创内容页面
	 * @param topicId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addOriginal")
	public String addOriginal(@RequestParam(value="topicId",required=true)Integer topicId,
							  @RequestParam(value="type",required=false)Integer type,
							  Model model){
		model.addAttribute("topicId", topicId);
		model.addAttribute("type", type);
		return "/topic/addoriginal";
	}

	/**
	 * 存储原创内容
	 * @param topicId
	 * @param title
	 * @param img
	 * @param gif
	 * @param content
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addOriginalContent", produces = {"application/json"})
	@ResponseBody
	public Result addOriginalContent(@RequestParam(value="topicId",required=true)Integer topicId,
									 @RequestParam(value="title",required=false)String title,
									 @RequestParam(value="img",required=false)String img,
									 @RequestParam(value="gif",required=false)String gif,
									 @RequestParam(value="content",required=false)String content,
									 Model model){
		try {
			boolean status = topicService.addOriginalContent(title, img, gif, content, topicId);
			if (status) {
				return new Success();
			} else {
				return new Failed("存储图片失败！");
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
			return new Failed();
		}
	}
}
