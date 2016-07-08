package com.oupeng.joke.back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;

@Controller
@RequestMapping(value="/channel")
public class ChannelController {
	
	@Autowired
	private ChannelService channelService;
	@Autowired
	private JokeService jokeService;
	
	@RequestMapping(value="/list")
	public String getChannelList(@RequestParam(value="status",required=false)Integer status,Model model){
		model.addAttribute("list", channelService.getChannelList(status));
		model.addAttribute("status", status);
		return "/channel/list";
	} 
	
	@RequestMapping(value="/verify")
	@ResponseBody
	public Result verify(@RequestParam(value="id")Integer id,
			@RequestParam(value="status")Integer status,
			Model model){
		String result = channelService.updateChannelStatus(id, status);
		if(result == null){
			return new Success();
		}else{
			return new Failed(result);
		}
	} 
	
	@RequestMapping(value="/edit")
	public String edit(@RequestParam(value="id")Integer id,Model model){
		model.addAttribute("channel", channelService.getChannelById(id));
		return "/channel/edit";
	} 
	
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id",required=true)Integer id,
			@RequestParam(value="name",required=false)String name,
			@RequestParam(value="type",required=true)Integer type,
			@RequestParam(value="contentType",required=false)String contentType){
		channelService.updateChannel(id, name, type, contentType);
		return new Success();
	} 
	
	@RequestMapping(value="/add")
	@ResponseBody
	public Result add(@RequestParam(value="name",required=false)String name,
			@RequestParam(value="type",required=true)Integer type,
			@RequestParam(value="contentType",required=false)String contentType){
		channelService.insertChannel(name, type, contentType);
		return new Success();
	} 
	
	@RequestMapping(value="/joke")
	public String getJokeList(@RequestParam(value="channelId",required=false)Integer channelId,
			@RequestParam(value="pageNumber",required=false)Integer pageNumber,
    		@RequestParam(value="pageSize",required=false)Integer pageSize,
			Model model){
		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
    	pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
    	int pageCount = 0;//总页数
    	int offset = 0 ;//开始条数index
    	List<Joke> list = null;
    	String contentType = null;
    	if(channelId != null){
    		contentType = channelService.getChannelById(channelId).getContentType();
    	}
    	int count = jokeService.getJokeCountForChannel(contentType);//总条数
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
			
			list = jokeService.getJokeListForChannel(contentType, offset, pageSize);
    	}
    	model.addAttribute("channelId", channelId);
    	model.addAttribute("count", count);
    	model.addAttribute("pageNumber", pageNumber);
    	model.addAttribute("pageSize", pageSize);
    	model.addAttribute("pageCount", pageCount);
    	model.addAttribute("list", list);
    	model.addAttribute("channelList", channelService.getChannelList(null));
		return "/channel/joke";
	} 
}
