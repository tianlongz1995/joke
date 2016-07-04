package com.oupeng.joke.back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;

@Controller
@RequestMapping(value="/channel")
public class ChannelController {
	
	@Autowired
	private ChannelService channelService;
	
	@RequestMapping(value="/list")
	public String getChannelList(@RequestParam(value="status",required=false)Integer status,Model model){
		List<Channel> list = channelService.getChannelList(status);
		model.addAttribute("list", list);
		model.addAttribute("status", status);
		return "/channel/list";
	} 
	
	@RequestMapping(value="/verify")
	@ResponseBody
	public Result verify(@RequestParam(value="id")Integer id,
			@RequestParam(value="status")Integer status,
			Model model){
		channelService.updateChannelStatus(id, status);
		return new Success();
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
	public Result searchJokeList(@RequestParam(value="name",required=false)String name,
			@RequestParam(value="type",required=true)Integer type,
			@RequestParam(value="contentType",required=false)String contentType){
		channelService.insertChannel(name, type, contentType);
		return new Success();
	} 
}
